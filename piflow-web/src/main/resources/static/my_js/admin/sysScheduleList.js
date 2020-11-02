function initDatatableSysSchedulePage(testTableId, url, searchInputId) {
    var table = "";
    layui.use('table', function () {
        table = layui.table;

        //Method-level rendering
        table.render({

            elem: '#' + testTableId
            , url: (web_drawingBoard + url)
            , headers: {
                Authorization: ("Bearer " + token)
            }
            , cols: [[
                {field: 'jobName', title: 'Name', sort: true},
                {field: 'jobClass', title: 'Class', sort: true},
                {field: 'cronExpression', title: 'Cron', sort: true},
                {
                    field: 'dataSourceType', title: 'Status', sort: true, templet: function (data) {
                        return data.status.text
                    }
                },
                {field: 'crtDttmString', title: 'CreateTime', sort: true},
                {
                    field: 'right', title: 'Actions', sort: true, height: 100, templet: function (data) {
                        return responseHandlerSysSchedule(data);
                    }
                }
            ]]
            , id: testTableId
            , page: true
        });
    });

    $("#" + searchInputId).bind('input propertychange', function () {
        searchMonitor(table, testTableId, searchInputId);
    });

    function searchMonitor(layui_table, layui_table_id, searchInputId) {
        //Perform overload
        layui_table.reload(layui_table_id, {
            page: {
                curr: 1 //Start again on page 1
            }
            , where: {param: $('#' + searchInputId).val()}
        }, 'data');
    }
}


//Results returned in the background
function responseHandlerSysSchedule(res) {
    var actionsHtmlStr = "";
    if (res) {
        var actions_btn_1 = '<a class="btn" '
            + 'title="Run this timed task once" '
            + 'href="javascript:void(0);" '
            + 'onclick="javascript:onceTask(\'' + res.id + '\');" '
            + 'style="margin-right: 2px;">'
            + '<i class="icon-tag icon-white"></i>'
            + '</a>';
        var actions_btn_2 = '<a class="btn" '
            + 'title="Run this timed task" '
            + 'href="javascript:void(0);" '
            + 'onclick="javascript:startTask(\'' + res.id + '\');" '
            + 'style="margin-right: 2px;">'
            + '<i class="icon-play icon-white"></i>'
            + '</a>';
        var actions_btn_3 = '<a class="btn" '
            + 'title="Stop this timed task" '
            + 'href="javascript:void(0);" '
            + 'onclick="javascript:stopTask(\'' + res.id + '\');" '
            + 'style="margin-right: 2px;">'
            + '<i class="icon-stop icon-white"></i>'
            + '</a>';
        var actions_btn_4 = '<a class="btn" '
            + 'title="Pause this timed task" '
            + 'href="javascript:void(0);" '
            + 'onclick="javascript:pauseTask(\'' + res.id + '\');" '
            + 'style="margin-right: 2px;">'
            + '<i class="icon-pause icon-white"></i>'
            + '</a>';
        var actions_btn_5 = '<a class="btn" '
            + 'title="Reply to this scheduled task" '
            + 'href="javascript:void(0);" '
            + 'onclick="javascript:resumeTask(\'' + res.id + '\');" '
            + 'style="margin-right: 2px;">'
            + '<i class="icon-repeat icon-white"></i>'
            + '</a>';
        var actions_btn_6 = '<a class="btn" '
            + 'title="Edit this timed task" '
            + 'href="javascript:void(0);" '
            + 'onclick="javascript:newSysScheduleWindow(\'' + res.id + '\');" '
            + 'style="margin-right: 2px;">'
            + '<i class="icon-edit icon-white"></i>'
            + '</a>';
        var actions_btn_7 = '<a class="btn" '
            + 'title="Remove this timed task" '
            + 'href="javascript:void(0);" '
            + 'onclick="javascript:deleteTask(\'' + res.id + '\',\'' + res.jobName + '\');" '
            + 'style="margin-right: 2px;">'
            + '<i class="icon-trash icon-white"></i>'
            + '</a>';
        if (res.status) {
            res.status = res.status.text;
            if ('RUNNING' === res.status) {
                actionsHtmlStr = '<div style="width: 100%; text-align: center" >'
                    + actions_btn_3
                    + actions_btn_6
                    + actions_btn_7
                    + '</div>';
            } else {
                actionsHtmlStr = '<div style="width: 100%; text-align: center" >'
                    + actions_btn_2
                    + actions_btn_6
                    + actions_btn_7
                    + '</div>';
            }
        }
    }
    return actionsHtmlStr;
}

function newSysScheduleWindow(id) {
    $("#buttonSysSchedule").attr("onclick", "");
    if (id) {
        ajaxRequest({
            cache: true,//Keep cached data
            type: "get",//Request type post
            url: "/sysSchedule/getScheduleById",//This is the name of the file where I receive data in the background.
            data: {scheduleId: id},
            async: false,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
            error: function (request) {//Operation after request failure
                layer.closeAll('page');
                layer.msg('open failed ', {icon: 2, shade: 0, time: 2000});
                return;
            },
            success: function (data) {//Operation after request successful
                var dataMap = JSON.parse(data);
                if (200 === dataMap.code) {
                    console.log(dataMap);
                    var sysScheduleVo = dataMap.sysScheduleVo;
                    $("#buttonSysSchedule").attr("onclick", "updateSysSchedule()");
                    $("#scheduleId").val(id);
                    $("#scheduleName").val(sysScheduleVo.jobName);
                    $("#scheduleClass").val(sysScheduleVo.jobClass);
                    $("#scheduleCron").val(sysScheduleVo.cronExpression);
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
                } else {
                    layer.msg('open failed', {icon: 2, shade: 0, time: 2000});
                }
            }
        });
    } else {
        $("#buttonSysSchedule").attr("onclick", "createTask()");
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
}

function checkSysScheduleInput(scheduleName, scheduleClass, scheduleCron, description) {
    $('#scheduleName').removeClass('error_class');
    $('#scheduleClass').removeClass('error_class');
    $('#scheduleCron').removeClass('error_class');
    if (scheduleName == '') {
        layer.msg('name Can not be empty', {icon: 2, shade: 0, time: 2000});
        $('#scheduleName').addClass('error_class');
        return false;
    }
    if (scheduleClass == '') {
        layer.msg('class Can not be empty', {icon: 2, shade: 0, time: 2000});
        $('#scheduleClass').addClass('error_class');
        return false;
    }
    if (scheduleCron == '') {
        layer.msg('cron Can not be empty', {icon: 2, shade: 0, time: 2000});
        $('#scheduleCron').addClass('error_class');
        return false;
    }
    /*if (description == '') {
        layer.msg('description不能为空！', {icon: 2, shade: 0, time: 2000});
        document.getElementById('description').focus();
        return false;
    } */
    return true;
}

function createTask() {
    var scheduleName = $("#scheduleName").val();
    var scheduleClass = $("#scheduleClass").val();
    var scheduleCron = $("#scheduleCron").val();
    if (checkSysScheduleInput(scheduleName, scheduleClass, scheduleCron))
        ajaxRequest({
            cache: true,//Keep cached data
            type: "get",//Request type post
            url: "/sysSchedule/createTask",//This is the name of the file where I receive data in the background.
            data: {
                jobName: scheduleName,
                jobClass: scheduleClass,
                cronExpression: scheduleCron
            },
            async: false,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
            error: function (request) {//Operation after request failure
                layer.closeAll('page');
                layer.msg('creation failed ', {icon: 2, shade: 0, time: 2000});
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
                    layer.msg('creation failed', {icon: 2, shade: 0, time: 2000});
                }
            }
        });
}

function updateSysSchedule() {
    var id = $("#scheduleId").val();
    var scheduleName = $("#scheduleName").val();
    var scheduleClass = $("#scheduleClass").val();
    var scheduleCron = $("#scheduleCron").val();

    if (checkSysScheduleInput(scheduleName, scheduleClass, scheduleCron))
        ajaxRequest({
            cache: true,//Keep cached data
            type: "get",//Request type post
            url: "/sysSchedule/updateTask",//This is the name of the file where I receive data in the background.
            data: {
                id: id,
                jobName: scheduleName,
                jobClass: scheduleClass,
                cronExpression: scheduleCron
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
                var dataMap = JSON.parse(data);
                if (200 === dataMap.code) {
                    layer.closeAll('page');
                    layer.msg('update success', {icon: 1, shade: 0, time: 2000}, function () {
                        location.reload();
                    });
                } else {
                    layer.msg('update failed ', {icon: 2, shade: 0, time: 2000});
                }
            }
        });
}

function onceTask(id) {
    developmentFunc();
}

//run
function startTask(id) {
    $('#fullScreen').show();
    ajaxRequest({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/sysSchedule/startTask",//This is the name of the file where I receive data in the background.
        data: {sysScheduleId: id},
        async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            //alert("Request Failed");
            layer.msg("Request Failed", {icon: 2, shade: 0, time: 2000}, function () {
                $('#fullScreen').hide();
            });
            return;
        },
        success: function (data) {//Operation after request successful
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                layer.msg(dataMap.errorMsg, {icon: 1, shade: 0, time: 2000}, function () {
                    location.reload();
                });
            } else {
                layer.msg("Startup failure：" + dataMap.errorMsg, {icon: 2, shade: 0, time: 2000}, function () {
                    location.reload();
                });
            }
            $('#fullScreen').hide();
        }
    });
}

function stopTask(id) {
    $('#fullScreen').show();
    ajaxRequest({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/sysSchedule/stopTask",//This is the name of the file where I receive data in the background.
        data: {sysScheduleId: id},
        async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            //alert("Request Failed");
            layer.msg("Request Failed", {icon: 2, shade: 0, time: 2000}, function () {
                $('#fullScreen').hide();
            });
            return;
        },
        success: function (data) {//Operation after request successful
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                layer.msg(dataMap.errorMsg, {icon: 1, shade: 0, time: 2000}, function () {
                    location.reload();
                });
            } else {
                layer.msg("Stop failure：" + dataMap.errorMsg, {icon: 2, shade: 0, time: 2000}, function () {
                    location.reload();
                });
            }
            $('#fullScreen').hide();
        }
    });
}

function pauseTask(id) {
    developmentFunc();
}

function resumeTask(id) {
    developmentFunc();
}

function deleteTask(id, name) {
    console.log(id);
    layer.confirm("Are you sure to delete '" + name + "' ?", {
        btn: ['confirm', 'cancel'] //button
        , title: 'Confirmation prompt'
    }, function () {
        ajaxRequest({
            cache: true,//Keep cached data
            type: "get",//Request type post
            url: "/sysSchedule/deleteTask",//This is the name of the file where I receive data in the background.
            data: {sysScheduleId: id},
            async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
            error: function (request) {//Operation after request failure
                return;
            },
            success: function (data) {//Operation after request successful
                console.log(data);
                var dataMap = JSON.parse(data);
                if (200 === dataMap.code) {
                    layer.msg('Delete Success', {icon: 1, shade: 0, time: 2000}, function () {
                        location.reload();
                    });
                } else {
                    layer.msg('Delete failed', {icon: 2, shade: 0, time: 2000});
                }
            }
        });
    }, function () {
    });
}

function developmentFunc() {
    layer.msg("Functional development", {icon: 5, shade: 0, time: 2000}, function () {
    });
}

