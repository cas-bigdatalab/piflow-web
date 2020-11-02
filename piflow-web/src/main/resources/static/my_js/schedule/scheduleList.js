function initDatatableSchedulePage(testTableId, url, searchInputId) {
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
                {field: 'type', title: 'Type', sort: true},
                {field: 'scheduleRunTemplateName', title: 'Name', sort: true},
                {field: 'cronExpression', title: 'Cron', sort: true},
                {field: 'planStartTimeStr', title: 'PlanStartTime', sort: true},
                {field: 'planEndTimeStr', title: 'PlanEndTime', sort: true},
                {
                    field: 'dataSourceType', title: 'Status', sort: true, templet: function (data) {
                        return data.status.text
                    }
                },
                {
                    field: 'right', title: 'Actions', sort: true, height: 100, templet: function (data) {
                        return responseHandlerSchedule(data);
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
function responseHandlerSchedule(res) {
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
            + 'onclick="javascript:startScheduleTask(\'' + res.id + '\');" '
            + 'style="margin-right: 2px;">'
            + '<i class="icon-play icon-white"></i>'
            + '</a>';
        var actions_btn_3 = '<a class="btn" '
            + 'title="Stop this timed task" '
            + 'href="javascript:void(0);" '
            + 'onclick="javascript:stopScheduleTask(\'' + res.id + '\');" '
            + 'style="margin-right: 2px;">'
            + '<i class="icon-stop icon-white"></i>'
            + '</a>';
        var actions_btn_4 = '<a class="btn" '
            + 'title="Edit this timed task" '
            + 'href="javascript:void(0);" '
            + 'onclick="javascript:schedulePopup(\'' + res.id + '\',\'update schedule\');"'
            + 'style="margin-right: 2px;">'
            + '<i class="icon-edit icon-white"></i>'
            + '</a>';
        var actions_btn_5 = '<a class="btn" '
            + 'title="Remove this timed task" '
            + 'href="javascript:void(0);" '
            + 'onclick="javascript:delScheduleTask(\'' + res.id + '\');" '
            + 'style="margin-right: 2px;">'
            + '<i class="icon-trash icon-white"></i>'
            + '</a>';
        if (res.status) {
            res.status = res.status.text;
            if ('RUNNING' === res.status) {
                actionsHtmlStr = '<div style="width: 100%; text-align: center" >'
                    + actions_btn_3
                    + actions_btn_4
                    + actions_btn_5
                    + '</div>';
            } else {
                actionsHtmlStr = '<div style="width: 100%; text-align: center" >'
                    + actions_btn_2
                    + actions_btn_3
                    + actions_btn_4
                    + actions_btn_5
                    + '</div>';
            }
        }
    }
    return actionsHtmlStr;
}

function createScheduleTask() {
    if ($("#schedule_cron").val()) {
        layer.msg("Cron cannot be empty");
    }
    if ($("#schedule_class").val()) {
        layer.msg("Flow or FlowGroup cannot be empty");
    }
    ajaxRequest({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/schedule/addSchedule",//This is the name of the file where I receive data in the background.
        data: {
            id: $("#scheduleId").val(),
            type: $("#schedule_type").val(),
            cronExpression: $("#schedule_cron").val(),
            planStartTimeStr: $("#schedule_plan_start_time").val(),
            planEndTimeStr: $("#schedule_plan_end_time").val(),
            scheduleRunTemplateId: $("#schedule_class").val()
        },
        async: false,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            layer.closeAll('page');
            layer.msg('open failed ', {icon: 2, shade: 0, time: 2000});
            return;
        },
        success: function (data) {//Operation after request successful
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                layer.msg(dataMap.errorMsg, {icon: 1, shade: 0, time: 2000}, function () {
                    window.location.reload();
                });
            } else {
                layer.msg(dataMap.errorMsg, {icon: 2, shade: 0, time: 2000});
            }
        }
    });
}

function updateScheduleTask(scheduleId) {
    if ($("#schedule_cron").val()) {
        layer.msg("Cron cannot be empty");
    }
    if ($("#schedule_class").val()) {
        layer.msg("Flow or FlowGroup cannot be empty");
    }
    ajaxRequest({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/schedule/updateSchedule",//This is the name of the file where I receive data in the background.
        data: {
            id: $("#scheduleId").val(),
            type: $("#schedule_type").val(),
            cronExpression: $("#schedule_cron").val(),
            planStartTimeStr: $("#schedule_plan_start_time").val(),
            planEndTimeStr: $("#schedule_plan_end_time").val(),
            scheduleRunTemplateId: $("#schedule_class").val()
        },
        async: false,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            layer.closeAll('page');
            layer.msg('open failed ', {icon: 2, shade: 0, time: 2000});
            return;
        },
        success: function (data) {//Operation after request successful
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                layer.msg(dataMap.errorMsg, {icon: 1, shade: 0, time: 2000}, function () {
                   window.location.reload();
                });
            } else {
                layer.msg(dataMap.errorMsg, {icon: 2, shade: 0, time: 2000});
            }
        }
    });
}

function startScheduleTask(scheduleId) {
    ajaxRequest({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/schedule/startSchedule",//This is the name of the file where I receive data in the background.
        data: {scheduleId: scheduleId},
        async: false,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            layer.closeAll('page');
            layer.msg('open failed ', {icon: 2, shade: 0, time: 2000});
            return;
        },
        success: function (data) {//Operation after request successful
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                layer.msg(dataMap.errorMsg, {icon: 1, shade: 0, time: 2000}, function () {
                    window.location.reload();
                });
            } else {
                layer.msg(dataMap.errorMsg, {icon: 2, shade: 0, time: 2000});
            }
        }
    });
}

function stopScheduleTask(scheduleId) {
    ajaxRequest({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/schedule/stopSchedule",//This is the name of the file where I receive data in the background.
        data: {scheduleId: scheduleId},
        async: false,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            layer.closeAll('page');
            layer.msg('open failed ', {icon: 2, shade: 0, time: 2000});
            return;
        },
        success: function (data) {//Operation after request successful
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                layer.msg(dataMap.errorMsg, {icon: 1, shade: 0, time: 2000}, function () {
                    window.location.reload();
                });
            } else {
                layer.msg(dataMap.errorMsg, {icon: 2, shade: 0, time: 2000});
            }
        }
    });
}

function delScheduleTask(scheduleId) {
    layer.confirm("Are you sure to delete ?", {
        btn: ['confirm', 'cancel'] //button
        , title: 'Confirmation prompt'
    }, function () {
        ajaxRequest({
            cache: true,//Keep cached data
            type: "POST",//Request type post
            url: "/schedule/delSchedule",//This is the name of the file where I receive data in the background.
            data: {scheduleId: scheduleId},
            async: false,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
            error: function (request) {//Operation after request failure
                layer.closeAll('page');
                layer.msg('open failed ', {icon: 2, shade: 0, time: 2000});
                return;
            },
            success: function (data) {//Operation after request successful
                var dataMap = JSON.parse(data);
                if (200 === dataMap.code) {
                    layer.msg(dataMap.errorMsg, {icon: 1, shade: 0, time: 2000}, function () {
                        window.location.reload();
                    });
                } else {
                    layer.msg(dataMap.errorMsg, {icon: 2, shade: 0, time: 2000});
                }
            }
        });
    });
}

function schedulePopup(id, titleName) {
    openLayerWindowLoadHtml('<div id="layer_open_content_id" style="display:none;"></div>', 580, 520, titleName, 0.3);
    ajaxLoadAsync("layer_open_content_id", "/page/schedule/schedule_new_or_update.html", true, function () {
        onloadScheduleInfoHtmlDate(id);
    });
}

function checkScheduleInput(scheduleCron, scheduleRunTemplateId, description) {
    $('#schedule_cron').removeClass('error_class');
    $('#scheduleRunTemplateId').removeClass('error_class');
    if (scheduleCron == '') {
        layer.msg('cron Can not be empty', {icon: 2, shade: 0, time: 2000});
        $('#schedule_cron').addClass('error_class');
        return false;
    }
    if (scheduleRunTemplateId == '') {
        layer.msg('flow or group Can not be empty', {icon: 2, shade: 0, time: 2000});
        $('#scheduleRunTemplateId').addClass('error_class');
        return false;
    }
    /*if (description == '') {
        layer.msg('description不能为空！', {icon: 2, shade: 0, time: 2000});
        document.getElementById('description').focus();
        return false;
    } */
    return true;
}

function onloadScheduleInfoHtmlDate(scheduleId) {
    $("#buttonSchedule").attr("onclick", "");
    if (scheduleId) {
        ajaxRequest({
            cache: true,//Keep cached data
            type: "get",//Request type post
            url: "/schedule/getScheduleById",//This is the name of the file where I receive data in the background.
            data: {scheduleId: scheduleId},
            async: false,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
            error: function (request) {//Operation after request failure
                layer.closeAll('page');
                layer.msg('open failed ', {icon: 2, shade: 0, time: 2000});
                return;
            },
            success: function (data) {//Operation after request successful
                var dataMap = JSON.parse(data);
                if (200 === dataMap.code) {
                    var scheduleVo = dataMap.scheduleVo;
                    $("#buttonSchedule").attr("onclick", "updateScheduleTask()");
                    $("#scheduleId").val(scheduleVo.id);
                    $("#schedule_class").val(scheduleVo.scheduleRunTemplateId);
                    $("#schedule_cron").val(scheduleVo.cronExpression);
                    $("#schedule_plan_start_time").val(scheduleVo.planStartTime);
                    $("#schedule_plan_end_time").val(scheduleVo.planEndTime);
                } else {
                    layer.msg('open failed', {icon: 2, shade: 0, time: 2000});
                }
            }
        });
    } else {
        $("#buttonSchedule").attr("onclick", "createScheduleTask()");
        $("#scheduleId").val("");
        $("#scheduleName").val("");
        $("#scheduleClass").val("");
        $("#schedule_cron").val("");
        $("#schedule_plan_start_time").val();
        $("#schedule_plan_end_time").val();
    }
}

function loadFlowOrGroupSelect() {
    var schedule_type = $("#schedule_type").val();
    var request_url = "/flow/getFlowListPage";
    if (schedule_type === "FLOW_GROUP") {
        request_url = "/flowGroup/getFlowGroupListPage";
    }
    console.log(schedule_type);
    $("#schedule_class").val("");
    ajaxRequest({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: request_url,//This is the name of the file where I receive data in the background.
        data: {page: 1, limit: 10000},
        async: false,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            layer.closeAll('page');
            layer.msg('open failed ', {icon: 2, shade: 0, time: 2000});
            return;
        },
        success: function (data) {//Operation after request successful
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                $("#schedule_class").html('<option value="">Please select</option>');
                var option_data = dataMap.data;
                if (dataMap.data) {
                    for (var i = 0; i < option_data.length; i++) {
                        var option_data_i = option_data[i];
                        $("#schedule_class").append('<option value="' + option_data_i.id + '">' + option_data_i.name + '</option>');
                    }
                }

            } else {
                layer.msg('open failed', {icon: 2, shade: 0, time: 2000});
            }
            $("#layer_open_content_id").show();
        }
    });
}

function developmentFunc() {
    layer.msg("Functional development", {icon: 5, shade: 0, time: 2000}, function () {
    });
}

