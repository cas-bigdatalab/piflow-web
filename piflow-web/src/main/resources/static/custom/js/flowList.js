var flowTable;

function newPath() {
    $("#buttonFlow").attr("onclick", "");
    $("#buttonFlow").attr("onclick", "saveFlow()");
    $("#flowId").val("");
    $("#flowName").val("");
    $("#description").val("");
    $("#driverMemory").val('1g');
    $("#executorNumber").val('1');
    $("#executorMemory").val('1g');
    $("#executorCores").val('1');
    layer.open({
        type: 1,
        title: '<span style="color: #269252;">create flow</span>',
        shadeClose: true,
        closeBtn: 1,
        shift: 7,
        area: ['580px', '520px'], //Width height
        skin: 'layui-layer-rim', //Add borders
        content: $("#SubmitPage")
    });
}

function update(id, updateName, updateDescription, driverMemory, executorNumber, executorMemory, executorCores) {
    $("#buttonFlow").attr("onclick", "");
    $("#buttonFlow").attr("onclick", "updateFlow()");
    $("#flowId").val(id);
    $("#flowName").val(updateName);
    $("#description").val(updateDescription);
    $("#driverMemory").val(driverMemory);
    $("#executorNumber").val(executorNumber);
    $("#executorMemory").val(executorMemory);
    $("#executorCores").val(executorCores);
    layer.open({
        type: 1,
        title: '<span style="color: #269252;">update flow</span>',
        shadeClose: true,
        closeBtn: false,
        shift: 7,
        closeBtn: 1,
        area: ['580px', '520px'], //Width height
        skin: 'layui-layer-rim', //Add borders
        content: $("#SubmitPage")
    });
}

function saveFlow() {
    var flowName = $("#flowName").val();
    var description = $("#description").val();
    var driverMemory = $("#driverMemory").val();
    var executorNumber = $("#executorNumber").val();
    var executorMemory = $("#executorMemory").val();
    var executorCores = $("#executorCores").val();
    if (checkFlowInput(flowName, description, driverMemory, executorNumber, executorMemory, executorCores))
        $.ajax({
            cache: true,//Keep cached data
            type: "get",//Request type post
            url: "/piflow-web/flow/saveFlowInfo",//This is the name of the file where I receive data in the background.
            data: {
                name: flowName,
                description: description,
                driverMemory: driverMemory,
                executorNumber: executorNumber,
                executorMemory: executorMemory,
                executorCores: executorCores
            },
            async: false,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
            error: function (request) {//Operation after request failure
                layer.closeAll('page');
                layer.msg('creation failed ', {icon: 2, shade: 0, time: 2000}, function () {
                });
                return;
            },
            success: function (data) {//Operation after request successful
                layer.closeAll('page');
                var dataMap = JSON.parse(data);
                if (200 === dataMap.code) {
                    layer.msg('create success ', {icon: 1, shade: 0, time: 2000}, function () {
                        var tempWindow = window.open('_blank');
                        if (tempWindow == null || typeof(tempWindow)=='undefined'){
                            alert('The window cannot be opened. Please check your browser settings.')
                        } else {
                            tempWindow.location = "/piflow-web/mxGraph/drawingBoard?drawingBoardType=TASK&load=" + dataMap.flowId;
                        }
                    });
                } else {
                    layer.msg('creation failed', {icon: 2, shade: 0, time: 2000}, function () {
                    });
                }
            }
        });
}

function updateFlow() {
    var id = $("#flowId").val();
    var flowName = $("#flowName").val();
    var description = $("#description").val();
    var driverMemory = $("#driverMemory").val();
    var executorNumber = $("#executorNumber").val();
    var executorMemory = $("#executorMemory").val();
    var executorCores = $("#executorCores").val();
    if (checkFlowInput(flowName, description, driverMemory, executorNumber, executorMemory, executorCores))
        $.ajax({
            cache: true,//Keep cached data
            type: "get",//Request type post
            url: "/piflow-web/flow/updateFlowInfo",//This is the name of the file where I receive data in the background.
            data: {
                id: id,
                name: flowName,
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
function runFlows(loadId, runMode) {
    $('#fullScreen').show();
    var data = {flowId: loadId}
    if (runMode) {
        data.runMode = runMode;
    }
    $.ajax({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/piflow-web/flow/runFlow",//This is the name of the file where I receive data in the background.
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
                    //Jump to monitoring page after successful startup
                    var tempWindow = window.open('_blank');
                    if (tempWindow == null || typeof(tempWindow)=='undefined'){
                        alert('The window cannot be opened. Please check your browser settings.')
                    } else {
                        tempWindow.location = "/piflow-web/process/getProcessById?processId=" + dataMap.processId;
                    }
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

function deleteFlow(id, name) {
    layer.confirm("Are you sure to delete '" + name + "' ?", {
        btn: ['confirm', 'cancel'] //Button
        , title: 'Confirmation prompt'
    }, function () {
        $.ajax({
            cache: true,//Keep cached data
            type: "get",//Request type post
            url: "/piflow-web/flow/deleteFlow",//This is the name of the file where I receive data in the background.
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

function checkFlowInput(flowName, description, driverMemory, executorNumber, executorMemory, executorCores) {
    if (flowName == '') {
        layer.msg('flowName Can not be empty', {icon: 2, shade: 0, time: 2000});
        document.getElementById('flowName').focus();
        return false;
    }

    /*  if (description == '') {
         layer.msg('description不能为空！', {icon: 2, shade: 0, time: 2000});
         document.getElementById('description').focus();
         return false;
     } */
    if (driverMemory == '') {
        layer.msg('driverMemory Can not be empty', {icon: 2, shade: 0, time: 2000});
        document.getElementById('driverMemory').focus();
        return false;
    }
    if (executorNumber == '') {
        layer.msg('executorNumber Can not be empty', {icon: 2, shade: 0, time: 2000});
        document.getElementById('executorNumber').focus();
        return false;
    }
    if (executorMemory == '') {
        layer.msg('executorMemory Can not be empty', {icon: 2, shade: 0, time: 2000});
        document.getElementById('executorMemory').focus();
        return false;
    }
    if (executorCores == '') {
        layer.msg('executorCores Can not be empty', {icon: 2, shade: 0, time: 2000});
        document.getElementById('executorCores').focus();
        return false;
    }
    return true;
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
            url: "/piflow-web/template/saveTemplate",//This is the name of the file where I receive data in the background.
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

function initDatatableFlowPage(testTableId, url) {
    flowTable = $('#' + testTableId).DataTable({
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
            "dataSrc": responseHandlerFlow
        },
        "columns": [
            {"mDataProp": "name"},
            {"mDataProp": "description"},
            {"mDataProp": "crtDttm"},
            {"mDataProp": "actions", 'sClass': "text-center"}
        ]

    });
}

//Results returned in the background
function responseHandlerFlow(res) {
    var resPageData = res.pageData;
    var pageData = []
    if (resPageData && resPageData.length > 0) {
        for (var i = 0; i < resPageData.length; i++) {
            var data1 = {
                "name": "",
                "description": "",
                "crtDttm": "",
                "actions": ""
            }
            if (resPageData[i]) {
                var descriptionHtmlStr = '<div ' +
                    'style="width: 85px;overflow: hidden;text-overflow:ellipsis;white-space:nowrap;" ' +
                    'data-toggle="tooltip" ' +
                    'data-placement="top" ' +
                    'title="' + resPageData[i].description + '">' +
                    resPageData[i].description +
                    '</div>';
                var actionsHtmlStr = '<div style="width: 100%; text-align: center" >' +
                    '<a class="btn" ' +
                    'href="/piflow-web/mxGraph/drawingBoard?drawingBoardType=TASK&load=' + resPageData[i].id + '"' +
                    'target="_blank" ' +
                    'style="margin-right: 2px;">' +
                    '<i class="icon-share-alt icon-white"></i>' +
                    '</a>' +
                    '<a class="btn" ' +
                    'href="javascript:void(0);" ' +
                    'onclick="javascript:update(\'' +
                    resPageData[i].id + '\',\'' +
                    resPageData[i].name + '\',\'' +
                    resPageData[i].description + '\',\'' +
                    resPageData[i].driverMemory + '\',\'' +
                    resPageData[i].executorNumber + '\',\'' +
                    resPageData[i].executorMemory + '\',\'' +
                    resPageData[i].executorCores + '\');" ' +
                    'style="margin-right: 2px;">' +
                    '<i class="icon-edit icon-white"></i>' +
                    '</a>' +
                    '<a class="btn" ' +
                    'href="javascript:void(0);" ' +
                    'onclick="javascript:runFlows(\'' + resPageData[i].id + '\');" ' +
                    'style="margin-right: 2px;">' +
                    '<i class="icon-play icon-white"></i>' +
                    '</a>' +
                    '<a class="btn" ' +
                    'href="javascript:void(0);" ' +
                    'onclick="javascript:runFlows(\'' + resPageData[i].id + '\',\'DEBUG\');" ' +
                    'style="margin-right: 2px;">' +
                    '<i class="fa-bug icon-white"></i>' +
                    '</a>' +
                    '<a class="btn" ' +
                    'href="javascript:void(0);" ' +
                    'onclick="javascript:deleteFlow(\'' +
                    resPageData[i].id + '\',\'' +
                    resPageData[i].name + '\');" ' +
                    'style="margin-right: 2px;">' +
                    '<i class="icon-trash icon-white"></i>' +
                    '</a>' +
                    '<a class="btn" ' +
                    'href="javascript:void(0);" ' +
                    'onclick="javascript:saveTableTemplate(\'' +
                    resPageData[i].id + '\',\'' +
                    resPageData[i].name + '\');" ' +
                    'style="margin-right: 2px;">' +
                    '<i class="icon-check icon-white"></i>' +
                    '</a>' +
                    '</div>';
                if (resPageData[i].name) {
                    data1.name = resPageData[i].name;
                }
                if (resPageData[i].crtDttm) {
                    data1.crtDttm = resPageData[i].crtDttm;
                }
                if (descriptionHtmlStr) {
                    data1.description = descriptionHtmlStr;
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

function searchFlowPage() {
    flowTable.ajax.reload();
}

//请求接口重新加载stops
function reloadStopsList() {
    var fullScreen = $('#fullScreen');
    fullScreen.show();
    $.ajax({
        data: {"load": ''},
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/piflow-web/stops/reloadStops",//This is the name of the file where I receive data in the background.
        error: function (request) {//Operation after request failure
            //fullScreen.hide();
            //alert("reload fail");
            layer.msg("reload fail", {icon: 2, shade: 0, time: 2000}, function () {
            });
            return;
        },
        success: function (data) {//Operation after request successful
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                //alert("reload success");
                layer.msg("reload success", {icon: 1, shade: 0, time: 2000}, function () {
                });
                fullScreen.hide();
            } else {
                //alert("reload fail");
                layer.msg("reload fail", {icon: 2, shade: 0, time: 2000}, function () {
                });
                fullScreen.hide();
            }
        }
    });
}
