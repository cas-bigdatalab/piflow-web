var scheduleTable;

function initDatatableSchedulePage(testTableId, url) {
    scheduleTable = $('#' + testTableId).DataTable({
        "pagingType": "full_numbers",//Set the mode of the paging control
        "searching": true,//Query the query box for datatales
        "aLengthMenu": [10, 20, 50, 100],//Set one page to display 10 records
        "bAutoWidth": true,
        "bLengthChange": true,//A drop-down list of how many records are displayed on a page of a blocked table
        "ordering": false, // Prohibit sorting
        "oLanguage": {
            "sSearch": "<span>Filter records:</span> _INPUT_",
            "sLengthMenu": "<span>Show entries:</span> _MENU_",
            "oPaginate": {"sFirst": "First", "sLast": "Last", "sNext": ">", "sPrevious": "<"}
        },
        "processing": true, //Open wait effect when data is loaded
        "serverSide": true,//Open background paging
        "ajax": {
            "url": url,
            "data": function (d) {
                var level1 = $('#level1').val();
                //Add additional parameters to the server
                d.extra_search = d.search.value;
            },
            "dataSrc": responseHandlerSchedule
        },
        "columns": [
            {"mDataProp": "name"},
            {"mDataProp": "class"},
            {"mDataProp": "cron"},
            {"mDataProp": "status"},
            {"mDataProp": "crtDttm"},
            {"mDataProp": "actions", 'sClass': "text-center"}
        ]

    });
}

//Results returned in the background
function responseHandlerSchedule(res) {
    console.log(res);
    var resPageData = res.pageData;
    var pageData = []
    if (resPageData && resPageData.length > 0) {
        for (var i = 0; i < resPageData.length; i++) {
            var data1 = {
                "name": "",
                "class": "",
                "cron": "",
                "status": "",
                "crtDttm": "",
                "actions": ""
            }
            if (resPageData[i]) {
                var descriptionHtmlStr = '<div ' +
                    'style="width: 85px;overschedule: hidden;text-overschedule:ellipsis;white-space:nowrap;" ' +
                    'data-toggle="tooltip" ' +
                    'data-placement="top" ' +
                    'title="' + resPageData[i].description + '">' +
                    resPageData[i].description +
                    '</div>';
                var actionsHtmlStr = '<div style="width: 100%; text-align: center" >'
                    + '<a class="btn" '
                    + 'title="Run this timed task" '
                    + 'href="javascript:void(0);" '
                    + 'onclick="javascript:runSchedules(\'' + resPageData[i].id + '\');" '
                    + 'style="margin-right: 2px;">'
                    + '<i class="icon-play icon-white"></i>'
                    + '</a>'
                    + '<a class="btn" '
                    + 'title="Pause this timed task" '
                    + 'href="javascript:void(0);" '
                    + 'onclick="javascript:runSchedules(\'' + resPageData[i].id + '\');" '
                    + 'style="margin-right: 2px;">'
                    + '<i class="icon-pause icon-white"></i>'
                    + '</a>'
                    + '<a class="btn" '
                    + 'title="Run this timed task once" '
                    + 'href="javascript:void(0);" '
                    + 'onclick="javascript:runSchedules(\'' + resPageData[i].id + '\',\'DEBUG\');" '
                    + 'style="margin-right: 2px;">'
                    + '<i class="icon-tag icon-white"></i>'
                    + '</a>'
                    + '<a class="btn" '
                    + 'title="Edit this timed task" '
                    + 'href="javascript:void(0);" '
                    + 'onclick="javascript:runSchedules(\'' + resPageData[i].id + '\');" '
                    + 'style="margin-right: 2px;">'
                    + '<i class="icon-edit icon-white"></i>'
                    + '</a>'
                    + '<a class="btn" '
                    + 'title="Edit this timed task" '
                    + 'href="javascript:void(0);" '
                    + 'onclick="javascript:deleteSchedule(\'' + resPageData[i].id + '\',\'' + resPageData[i].name + '\');" '
                    + 'style="margin-right: 2px;">'
                    + '<i class="icon-trash icon-white"></i>'
                    + '</a>'
                    /*
                    + '<a class="btn" '
                    + 'title="Reply to this scheduled task" '
                    + 'href="javascript:void(0);" '
                    + 'onclick="javascript:saveTableTemplate(\''
                    + resPageData[i].id + '\',\''
                    + resPageData[i].name + '\');" '
                    + 'style="margin-right: 2px;">'
                    + '<i class="icon-repeat icon-white"></i>'
                    + '</a>'
                    */
                    + '</div>';
                if (resPageData[i].jobName) {
                    data1.name = resPageData[i].jobName;
                }
                if (resPageData[i].jobClass) {
                    data1.class = resPageData[i].jobClass;
                }
                if (resPageData[i].cronExpression) {
                    data1.cron = resPageData[i].cronExpression;
                }
                if (resPageData[i].status) {
                    data1.status = resPageData[i].status.text;
                }
                if (resPageData[i].crtDttmString) {
                    data1.crtDttm = resPageData[i].crtDttmString;
                }
                if (actionsHtmlStr) {
                    data1.actions = actionsHtmlStr;
                }
            }
            pageData.push(data1);
        }
    }
    return pageData;
}

function searchSchedulePage() {
    flowTable.ajax.reload();
}

function newScheduleWindow() {
    $("#buttonSchedule").attr("onclick", "");
    $("#buttonSchedule").attr("onclick", "createSchedule()");
    $("#scheduleId").val("");
    $("#scheduleName").val("");
    $("#scheduleClass").val("");
    $("#scheduleCron").val("");
    layer.open({
        type: 1,
        title: '<span style="color: #269252;">create schedule</span>',
        shadeClose: true,
        closeBtn: 1,
        shift: 7,
        area: ['580px', '520px'], //Width height
        skin: 'layui-layer-rim', //Add borders
        content: $("#SubmitPage")
    });
}

function update(id, updateName, updateDescription, driverMemory, executorNumber, executorMemory, executorCores) {
    $("#buttonSchedule").attr("onclick", "");
    $("#buttonSchedule").attr("onclick", "updateSchedule()");
    $("#scheduleId").val(id);
    $("#scheduleName").val(updateName);
    $("#description").val(updateDescription);
    $("#driverMemory").val(driverMemory);
    $("#executorNumber").val(executorNumber);
    $("#executorMemory").val(executorMemory);
    $("#executorCores").val(executorCores);
    layer.open({
        type: 1,
        title: '<span style="color: #269252;">update schedule</span>',
        shadeClose: true,
        closeBtn: false,
        shift: 7,
        closeBtn: 1,
        area: ['580px', '520px'], //Width height
        skin: 'layui-layer-rim', //Add borders
        content: $("#SubmitPage")
    });
}

function checkScheduleInput(scheduleName, scheduleClass, scheduleCron, description) {
    if (scheduleName == '') {
        layer.msg('scheduleName Can not be empty', {icon: 2, shade: 0, time: 2000});
        document.getElementById('scheduleName').focus();
        return false;
    }
    if (scheduleClass == '') {
        layer.msg('scheduleClass Can not be empty', {icon: 2, shade: 0, time: 2000});
        document.getElementById('scheduleClass').focus();
        return false;
    }
    if (scheduleCron == '') {
        layer.msg('scheduleCron Can not be empty', {icon: 2, shade: 0, time: 2000});
        document.getElementById('scheduleCron').focus();
        return false;
    }
    /*if (description == '') {
        layer.msg('description不能为空！', {icon: 2, shade: 0, time: 2000});
        document.getElementById('description').focus();
        return false;
    } */
    return true;
}

function createSchedule() {
    var scheduleName = $("#scheduleName").val();
    var scheduleClass = $("#scheduleClass").val();
    var scheduleCron = $("#scheduleCron").val();
    if (checkScheduleInput(scheduleName, scheduleClass, scheduleCron))
        $.ajax({
            cache: true,//Keep cached data
            type: "get",//Request type post
            url: "/piflow-web/sysSchedule/createSchedule",//This is the name of the file where I receive data in the background.
            data: {
                jobName: scheduleName,
                jobClass: scheduleClass,
                cronExpression: scheduleCron
            },
            async: false,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
            error: function (request) {//Operation after request failure
                layer.closeAll('page');
                layer.msg('creation failed ', {icon: 2, shade: 0, time: 2000}, function () {
                });
                return;
            },
            success: function (data) {//Operation after request successful
                var dataMap = JSON.parse(data);
                if (200 === dataMap.code) {
                    layer.closeAll('page');
                    layer.msg('create success ', {icon: 1, shade: 0, time: 2000}, function () {
                        location.reload();
                    });
                } else {
                    layer.msg('creation failed', {icon: 2, shade: 0, time: 2000}, function () {
                    });
                }
            }
        });
}

function updateSchedule() {
    var id = $("#scheduleId").val();
    var scheduleName = $("#scheduleName").val();
    var description = $("#description").val();
    var driverMemory = $("#driverMemory").val();
    var executorNumber = $("#executorNumber").val();
    var executorMemory = $("#executorMemory").val();
    var executorCores = $("#executorCores").val();
    if (checkScheduleInput(scheduleName, description, driverMemory, executorNumber, executorMemory, executorCores))
        $.ajax({
            cache: true,//Keep cached data
            type: "get",//Request type post
            url: "/piflow-web/schedule/updateScheduleInfo",//This is the name of the file where I receive data in the background.
            data: {
                id: id,
                name: scheduleName,
                description: description,
                driverMemory: driverMemory,
                executorNumber: executorNumber,
                executorMemory: executorMemory,
                executorCores: executorCores
            },
            async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
            error: function (request) {//Operation after request failure
                layer.closeAll('page');
                layer.msg('update failed ', {icon: 2, shade: 0, time: 2000}, function () {
                    window.location.reload();
                });
                return;
            },
            success: function (data) {//Operation after request successful
                if (data > 0) {
                    layer.closeAll('page');
                    layer.msg('update success', {icon: 1, shade: 0, time: 2000}, function () {
                        location.reload();
                    });
                }
            }
        });
}

//run
function runSchedules(loadId, runMode) {
    $('#fullScreen').show();
    var data = {scheduleId: loadId}
    if (runMode) {
        data.runMode = runMode;
    }
    $.ajax({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/piflow-web/schedule/runSchedule",//This is the name of the file where I receive data in the background.
        //data:$('#loginForm').serialize(),//Serialize the form
        data: data,
        async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            //alert("Request Failed");
            layer.msg("Request Failed", {icon: 2, shade: 0, time: 2000}, function () {
                $('#fullScreen').hide();
            });

            return;
        },
        success: function (data) {//Operation after request successful
            //console.log("success");
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                layer.msg(dataMap.errorMsg, {icon: 1, shade: 0, time: 2000}, function () {
                    //启动成功后跳转至监控页面
                    var tempwindow = window.open('_blank');
                    tempwindow.location = "/piflow-web/process/getProcessById?processId=" + dataMap.processId;
                });
            } else {
                //alert("Startup failure：" + dataMap.errorMsg);
                layer.msg("Startup failure：" + dataMap.errorMsg, {icon: 2, shade: 0, time: 2000}, function () {
                    location.reload();
                });
            }
            $('#fullScreen').hide();
        }
    });
}

function deleteSchedule(id, name) {
    layer.confirm("Are you sure to delete '" + name + "' ?", {
        btn: ['confirm', 'cancel'] //按钮
        , title: 'Confirmation prompt'
    }, function () {
        $.ajax({
            cache: true,//Keep cached data
            type: "get",//Request type post
            url: "/piflow-web/flow/deleteSchedule",//This is the name of the file where I receive data in the background.
            data: {id: id},
            async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
            error: function (request) {//Operation after request failure
                return;
            },
            success: function (data) {//Operation after request successful
                if (data > 0) {
                    layer.msg('Delete Success', {icon: 1, shade: 0, time: 2000}, function () {
                        location.reload();
                    });
                } else {
                    layer.msg('Delete failed', {icon: 2, shade: 0, time: 2000}, function () {
                    });
                }
            }
        });
    }, function () {
    });
}

function saveTableTemplate(id, name) {
    layer.prompt({
        title: 'please enter the template name',
        formType: 0,
        btn: ['submit', 'cancel']
    }, function (text, index) {
        layer.close(index);
        $.ajax({
            cache: true,//Keep cached data
            type: "get",//Request type post
            url: "/pischedule-web/template/saveTemplate",//This is the name of the file where I receive data in the background.
            data: {load: id, name: text},
            async: true,
            error: function (request) {//Operation after request failure
                console.log(" save template error");
                return;
            },
            success: function (data) {//Operation after request successful
                var dataMap = JSON.parse(data);
                if (200 === dataMap.code) {
                    layer.msg(dataMap.errorMsg, {icon: 1, shade: 0, time: 2000}, function () {
                    });
                } else {
                    layer.msg(dataMap.errorMsg, {icon: 2, shade: 0, time: 2000}, function () {
                    });
                }
            }
        });
    });
}

