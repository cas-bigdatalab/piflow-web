var fullScreen = $('#fullScreen');
var runFlowBtn = $('#runFlow');
var debugFlowBtn = $('#debugFlow');
var stopFlowBtn = $('#stopFlow');
var processContent = $('#processContent');
var checkpointShow = $('#checkpointShow');
var isLoadProcessInfo = true;
var isEnd = false;

function selectedFormation(pageId, e) {
    if (isLoadProcessInfo) {
        isLoadProcessInfo = false;
    }
    if (e && pageId) {
        var selectedRectShow = $('#selectedRectShow');
        if (selectedRectShow) {
            var imgStyleX = 0;
            var imgStyleY = 0;
            var imgStyleWidth = 66;
            var imgStyleHeight = 66;
            if (e) {
                var selectedStop = $(e).find('#stopImg' + pageId);
                imgStyleX = $(selectedStop).attr('x');
                imgStyleY = $(selectedStop).attr('y');
                imgStyleWidth = $(selectedStop).attr('width');
                imgStyleHeight = $(selectedStop).attr('height');
            }
            selectedRectShow.attr('x', imgStyleX).attr('y', imgStyleY).attr('width', imgStyleWidth).attr('height', imgStyleHeight);
        }
        queryProcessStop(processId, pageId);
    } else {
        //alert("Necessary position parameters were not obtained");
        layer.msg("Necessary position parameters were not obtained", {icon: 2, shade: 0, time: 2000}, function () {
        });
    }
}

function selectedPath(pageId, e) {
    if (isLoadProcessInfo) {
        isLoadProcessInfo = false;
    }
    if (pageId) {
        var selectedPathShow = $('#selectedPathShow');
        var selectedArrowShow = $('#selectedArrowShow');
        var pathStyleD = 'M 0 0 L 0 0';
        var arrowStyleD = 'M 0 0 L 0 0 L 0 0 L 0 0 Z';
        //var paths = $(e).find('path[name="arrowName"]');
        if (e) {
            pathStyleD = $(e).find('path[name="arrowName"]').attr("d");
            arrowStyleD = $(e).find('path[name="pathName"]').attr("d");
        }
        if (selectedPathShow) {
            selectedPathShow.attr('d', pathStyleD);
            selectedArrowShow.attr('d', arrowStyleD);
            selectedPathShow.show();
            selectedArrowShow.show();
            //selectedPolygonShow.attr('x', x).attr('y', y).attr('width', width).attr('height', height);
        }
        queryProcessPath(processId, pageId);
    } else {
        //alert("The necessary position parameters are not obtained");
        layer.msg("Necessary position parameters were not obtained", {icon: 2, shade: 0, time: 2000}, function () {
        });
    }
}

// Query process basic information
function queryProcess(processId) {
    if (!isLoadProcessInfo) {
        isLoadProcessInfo = true;
    } else {
        if (!processId || '' === processId || 'null' === processId || 'NULL' === processId) {
            //alert("Id is empty, not obtained, please check!!");
            layer.msg("Id is empty, not obtained, please check!!", {icon: 2, shade: 0, time: 2000}, function () {
            });
        } else {
            $.ajax({
                cache: true,//Keep cached data
                type: "POST",//Request type post
                url: "/piflow-web/process/queryProcess",//This is the name of the file where I receive data in the background.
                //data:$('#loginForm').serialize(),//Serialize the form
                data: {
                    processId: processId
                },
                async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
                error: function (request) {//Operation after request failure
                    console.log("fail");
                    return;
                },
                success: function (data) {//Operation after request successful
                    // console.log(data);
                    $('#processLeft').html(data)
                    $('#selectedRectShow').hide();
                    $('#selectedPathShow').hide();
                    $('#selectedArrowShow').hide();
                }
            });
        }
    }
    return;
}

//Query basic information about stops
function queryProcessStop(processId, pageId) {
    $.ajax({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/piflow-web/process/queryProcessStop",//This is the name of the file where I receive data in the background.
        //data:$('#loginForm').serialize(),//Serialize the form
        data: {
            processId: processId,
            pageId: pageId
        },
        async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            console.log("fail");
            return;
        },
        success: function (data) {//Operation after request successful
            // console.log(data);
            $('#processLeft').html(data);
            $('#selectedRectShow').show();
            $('#selectedArrowShow').hide();
            $('#selectedPathShow').hide();
            var sotpBundelShowText = $('#sotpBundelShow').text()
            if (sotpBundelShowText && sotpBundelShowText.toUpperCase() === "CN.PIFLOW.BUNDLE.HTTP.OPENURL") {
                var open_action = $('.open_action');
                if (open_action.length === 1) {
                    var open_action_i = $(open_action.get(0));
                    var a_href = open_action_i.text();
                    $(open_action.get(0)).after('&nbsp;&nbsp;&nbsp;&nbsp;<a class="btn btn-primary" href="' + a_href + '" style="color:#ffffff;" target="_blank">OPEN</a>');
                }
            }
        }
    });
}

//Query Path basic information
function queryProcessPath(processId, pageId) {
    if (isLoadProcessInfo) {
        isLoadProcessInfo = false;
    }
    $.ajax({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/piflow-web/process/queryProcessPath",//This is the name of the file where I receive data in the background.
        //data:$('#loginForm').serialize(),//Serialize the form
        data: {
            processId: processId,
            pageId: pageId
        },
        async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            console.log("fail");
            return;
        },
        success: function (data) {//Operation after request successful
            // console.log(data);
            $('#processLeft').html(data);
            $('#selectedArrowShow').show();
            $('#selectedPathShow').show();
            $('#selectedRectShow').hide();
        }
    });
}

//  Get Checkpoint points
function getCheckpoint(runMode) {
    fullScreen.show();
    $.ajax({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/piflow-web/process/getCheckpoint",//This is the name of the file where I receive data in the background.
        //data:$('#loginForm').serialize(),//Serialize the form
        data: {
            pID: pID,
            parentProcessId: parentProcessId
        },
        async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            runFlowBtn.show();
            debugFlowBtn.show();
            //alert("Request Failed");
            layer.msg("Request Failed", {icon: 2, shade: 0, time: 2000}, function () {
            });
            fullScreen.hide();
            checkpointShow.modal('hide');
            return;
        },
        success: function (data) {//Operation after request successful
            console.log("success");
            $('#checkpointContentNew').html(data);
            if ($('#checkpointsIsNull').val()) {
                runProcess(runMode);
            } else {
                runFlowBtn.show();
                debugFlowBtn.show();
                fullScreen.hide();

                if ("DEBUG" === runMode) {
                    $("#debug_checkpoint_new").show();
                    $("#run_checkpoint_new").hide();
                } else {
                    $("#debug_checkpoint_new").hide();
                    $("#run_checkpoint_new").show();
                }
                layer.open({
                    type: 1,
                    title: '<span style="color: #269252;">Select Run Mode</span>',
                    shadeClose: true,
                    closeBtn: 1,
                    shift: 7,
                    //area: ['600px', '200px'], //Width height
                    skin: 'layui-layer-rim', //Add borders
                    area: ['600px', ($("#layer_open_checkpoint").height() + 73) + 'px'], //Width Height
                    content: $("#layer_open_checkpoint").html()
                });

            }
        }
    });

}

function cancelRunProcess() {
    checkpointShow.modal('hide');
    fullScreen.hide();
    return;
}

//run
function runProcess(runMode) {
    fullScreen.show();
    checkpointShow.modal('hide');
    runFlowBtn.hide();
    debugFlowBtn.hide();
    var checkpointStr = '';
    $(".layui-layer-content").find("#checkpointContentNew").find("input[type='checkbox']:checked").each(function () {
        if ('' !== checkpointStr) {
            checkpointStr = (checkpointStr + ',');
        }
        checkpointStr = (checkpointStr + $(this).val());
    });
    var data = {
        id: processId,
        checkpointStr: checkpointStr
    };
    if (runMode) {
        data.runMode = runMode;
    }
    $.ajax({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/piflow-web/process/runProcess",//This is the name of the file where I receive data in the background.
        //data:$('#loginForm').serialize(),//Serialize the form
        data: data,
        async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            runFlowBtn.show();
            debugFlowBtn.show();
            //alert("Request Failed");
            layer.msg("Request Failed", {icon: 2, shade: 0, time: 2000}, function () {
            });
            fullScreen.hide();
            return;
        },
        success: function (data) {//Operation after request successful
            //console.log("success");
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                //alert(dataMap.errorMsg);
                layer.msg(dataMap.errorMsg, {icon: 1, shade: 0, time: 2000}, function () {
                });
                // window.location.href = "/piflow-web/process/getProcessById?processId=" + dataMap.processId;
                window.location.href = "/piflow-web/mxGraph/drawingBoard?drawingBoardType=PROCESS&processType=PROCESS&load=" + dataMap.processId;
            } else {
                //alert(dataMap.errorMsg);
                layer.msg(dataMap.errorMsg, {icon: 2, shade: 0, time: 2000}, function () {
                });
                runFlowBtn.show();
                debugFlowBtn.show();
                fullScreen.hide();
            }

        }
    });
}

//stop
function stopProcess() {
    stopFlowBtn.hide();
    fullScreen.show();
    $.ajax({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/piflow-web/process/stopProcess",//This is the name of the file where I receive data in the background.
        //data:$('#loginForm').serialize(),//Serialize the form
        data: {
            processId: processId
        },
        async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            stopFlow.show();
            //alert("Request Failed");
            layer.msg("Request Failed", {icon: 2, shade: 0, time: 2000}, function () {
            });
            fullScreen.hide();
            return;
        },
        success: function (data) {//Operation after request successful
            //console.log("success");
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                //alert(dataMap.errorMsg);
                layer.msg(dataMap.errorMsg, {icon: 1, shade: 0, time: 2000}, function () {
                });
                runFlowBtn.show();
                debugFlowBtn.show();
            } else {
                //alert("Stop Failed:" + dataMap.errorMsg);
                layer.msg("Stop Failed", {icon: 2, shade: 0, time: 2000}, function () {
                });
                stopFlow.show();
            }
            fullScreen.hide();
        }
    });
}

function getLogUrl() {
    var window_width = $(window).width();//Get browser window width
    var window_height = $(window).height();//Get browser window width
    var open_window_width = (window_width > 300 ? window_width - 200 : window_width);
    var open_window_height = (window_height > 300 ? window_height - 150 : window_height);
    var logContent = '<div id="divPreId" style="height: ' + (open_window_height - 121) + 'px;width: 100%;">'
        + '<div id="preId" style="height: 100%; margin: 6px 6px 6px 6px;background-color: #f5f5f5;text-align: center;">'
        + '<span span style="font-size: 90px;margin-top: 15px;">loading....</span>'
        + '</div>'
        + '</div>';

    // + '<div style="margin-top: 5px;margin-bottom: 5px;margin-left: 10px;">'
    // + '<input type="button" class="btn btn-default" onclick="changeUrl(1)" value="stdout">'
    // + '<input type="button" class="btn btn-default" onclick="changeUrl(2)" value="stderr">'
    // + '</div>';

    $.ajax({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/piflow-web/process/getLogUrl",//This is the name of the file where I receive data in the background.
        //data:$('#loginForm').serialize(),//Serialize the form
        data: {"appId": appId},
        async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            layer.msg("Request Failed", {icon: 2, shade: 0, time: 2000}, function () {
            });
            return;
        },
        success: function (data) {//Operation after request successful
            layer.open({
                type: 1,
                title: '<span style="color: #269252;">Log Windows</span>',
                shadeClose: true,
                closeBtn: 1,
                shift: 7,
                area: [open_window_width + 'px', open_window_height + 'px'], //Width height
                skin: 'layui-layer-rim', //Add borders
                btn: ['stdout', 'stderr'], //button
                btn1: function (index, layero) {
                    changeUrl(1);
                    return false;
                },
                btn2: function (index, layero) {
                    changeUrl(2);
                    return false;
                },
                content: logContent,
                success: function (layero) {
                    layero.find('.layui-layer-btn').css('text-align', 'left');
                    var bleBtn0 = layero.find('.layui-layer-btn0');
                    var bleBtn1 = layero.find('.layui-layer-btn1');
                    bleBtn0.removeClass('layui-layer-btn0');
                    bleBtn1.removeClass('layui-layer-btn1');
                    bleBtn0.addClass('layui-layer-btn1');
                    bleBtn1.addClass('layui-layer-btn1');
                }
            });
            if (200 === data.code) {
                stdoutLog = data.stdoutLog;
                stderrLog = data.stderrLog;
                changeUrl(1);
            }
        }
    });
}

function changeUrl(key) {
    var url
    switch (key) {
        case 1:
            url = stdoutLog;
            break;
        case 2:
            url = stderrLog;
            break;
        default:
            break;
    }
    $.ajax({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/piflow-web/process/getLog",//This is the name of the file where I receive data in the background.
        //data:$('#loginForm').serialize(),//Serialize the form
        data: {url: url},
        async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            return;
        },
        success: function (data) {//Operation after request successful
            console.log("success");
            if ('' !== data) {
                var content_td = $(data).find('.content')[0].innerHTML;
                var content_td_html = (('' !== content_td) ? content_td[0] : '');
                var showLogHtml = ('<div id="preId" style="height: 100%; margin: 6px 6px 6px 6px;background-color: #f5f5f5;">') + (content_td) + ('</div>');
                $("#divPreId").html(showLogHtml);
                $(".layui-layer-content").scrollTop($(".layui-layer-content")[0].scrollHeight);
            } else {
                $("#divPreId").html('<div id="preId" style="height: 100%; margin: 6px 6px 6px 6px;background-color: #f5f5f5;text-align: center;"><span style="font-size: 90px;margin-top: 15px;">Load Log Filed</span></div>');
            }
        }
    });
}

function processMonitoring(appId) {
    if (appId === '') {
        return;
    }
    $.ajax({
        cache: true,
        type: "get",
        url: "/piflow-web/process/getAppInfo",
        data: {appid: appId},
        async: true,
        traditional: true,
        error: function (request) {
            console.log("error");
            return;
        },
        success: function (data) {
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                if (dataMap.state && "" !== dataMap.state) {
                    if ("COMPLETED" === dataMap.state || "FAILED" === dataMap.state || "KILLED" === dataMap.state) {
                        window.clearInterval(timer);
                        runFlowBtn.show();
                        debugFlowBtn.show();
                        stopFlowBtn.hide();
                    }
                }
                var processVo = dataMap.processVo;
                if (processVo && '' != processVo) {
                    $("#progress").html(dataMap.progress + "%");
                    $("#processStartTimeShow").html(processVo.startTime);
                    $("#processStopTimeShow").html(processVo.endTime);
                    $("#processStateShow").html(dataMap.state);
                    $("#processProgressShow").html(dataMap.progress + "%");
                    // stop
                    var processStopVoList = processVo.processStopVoList;
                    if (processStopVoList && '' != processStopVoList) {
                        for (var i = 0; i < processStopVoList.length; i++) {
                            var processStopVo = processStopVoList[i];
                            if (processStopVo && '' != processStopVo) {
                                var sotpNameDB = processStopVo.name;
                                var sotpNameVal = $("#stopNameShow").text();
                                if (sotpNameDB === sotpNameVal) {
                                    $("#stopStartTimeShow").html(processStopVo.startTime);
                                    $("#stopStopTimeShow").html(processStopVo.endTime);
                                    $("#stopStateShow").html(processStopVo.state);
                                }
                                var pageId = processStopVo.pageId;
                                var processStopVoState = processStopVo.state;
                                monitor(pageId, processStopVoState)
                            }
                        }
                    }
                }
            }
        }
    });
}

function monitor(pageId, processStopVoState) {
    var stopFailShow = $("#stopFailShow" + pageId);
    var stopOkShow = $("#stopOkShow" + pageId);
    var stopLoadingShow = $("#stopLoadingShow" + pageId);
    var stopImgChange = $("#stopImg" + pageId);
    if (processStopVoState) {
        if (processStopVoState !== "INIT") {
            stopImgChange.attr('opacity', 1);
            if (processStopVoState && (processStopVoState === "STARTED")) {
                stopFailShow.hide();
                stopOkShow.hide();
                stopLoadingShow.show();
            } else if (processStopVoState && processStopVoState === "COMPLETED") {
                stopFailShow.hide();
                stopLoadingShow.hide();
                stopOkShow.show();
            } else if (processStopVoState && processStopVoState === "FAILED") {
                stopOkShow.hide();
                stopLoadingShow.hide();
                stopFailShow.show();
            }
        } else {
            stopImgChange.attr('opacity', 0.4);
        }
    } else {
        stopImgChange.attr('opacity', 0.4);
    }

    /*
    var stopPath = $('g[name="stopPageId' + pageId + '"]');
    if (stopPath.length > 0) {
        for (var i = 0; i < stopPath.length; i++) {
            var pathName = $(stopPath[i]).find('path[name="pathName"]');
            var arrowName = $(stopPath[i]).find('path[name="arrowName"]');
            if (processStopVoState && (processStopVoState === "STARTED")) {

            } else if (processStopVoState && processStopVoState === "COMPLETED") {
                $(pathName).attr('stroke', 'green');
                $(arrowName).attr('stroke', 'green').attr('fill', 'green');
            } else if (processStopVoState && processStopVoState === "FAILED") {
                $(pathName).attr('stroke', 'red');
                $(arrowName).attr('stroke', 'red').attr('fill', 'red');
            }
        }
    }
    */
}

function getDebugData(stopName, portName) {
    var window_width = $(window).width();//Get browser window width
    var window_height = $(window).height();//Get browser window width
    var jsonData = {"appId": appId, "stopName": stopName, "portName": portName};
    $.ajax({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/piflow-web/process/getDebugDataHtml",//This is the name of the file where I receive data in the background.
        //data:$('#loginForm').serialize(),//Serialize the form
        data: jsonData,
        async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            layer.msg("Request Failed", {icon: 2, shade: 0, time: 2000}, function () {
            });
            return;
        },
        success: function (data) {//Operation after request successful
            var open_window_width = (window_width > 300 ? window_width - 200 : window_width) + "px";
            var open_window_height = (window_height > 300 ? window_height - 200 : window_height) + "px";
            layer.open({
                type: 1,
                title: '<span style="color: #269252;">Debug Data</span>',
                shadeClose: true,
                closeBtn: 1,
                shift: 7,
                area: [open_window_width, open_window_height], //Width height
                skin: 'layui-layer-rim', //Add borders
                content: data
            });
        }
    });
}

function loadDebugData() {
    var debug_app_id = $("#debug_app_id");
    var debug_stop_name = $("#debug_stop_name");
    var debug_port_name = $("#debug_port_name");
    var debug_data_last_read_line = $("#debug_data_last_read_line");
    var debug_data_last_file_name = $("#debug_data_last_file_name");
    $.ajax({
        type: "POST",
        async: false,
        url: '/piflow-web/process/getDebugData', //Actual use, please change to the server real interface
        data: {
            "appId": debug_app_id.html(),
            "stopName": debug_stop_name.html(),
            "portName": debug_port_name.html(),
            "startFileName": debug_data_last_file_name.html(),
            "startLine": debug_data_last_read_line.html()
        },
        error: function () {
            layer.msg('An error occurred')
            return false;
        },
        success: function (rtnData) {
            var dataMap = JSON.parse(rtnData);
            if (200 === dataMap.code) {
                var div_table_list_obj = $('#div_table_list');
                var div_table_list_obj_children_length_add_1 = div_table_list_obj.children().length + 1;
                var debugData = dataMap.debugData;
                debug_data_last_read_line.html(debugData.lastReadLine);
                debug_data_last_file_name.html(debugData.lastFileName);
                isEnd = debugData.end;
                var schemaList = debugData.schema;
                var debugDataList = debugData.data;
                if (schemaList && schemaList.length > 0 && debugDataList && debugDataList.length > 0) {
                    var debug_data_table_id_obj = '<table id="debug_data_table_id_' + div_table_list_obj_children_length_add_1 + '" class="layui-table">';
                    var table_tr_th_all = '<thead><tr style="color: #1A7444;">';
                    for (var i = 0; i < schemaList.length; i++) {
                        table_tr_th_all += ('<th style="font-weight: bold;">' + schemaList[i] + '</th>');
                    }
                    table_tr_th_all += '</tr></thead>';
                    debug_data_table_id_obj += table_tr_th_all;
                    var table_tr_td_all = '';
                    for (var i = 0; i < debugDataList.length; i++) {
                        var debug_data_list_i_obj = JSON.parse(debugDataList[i]);
                        table_tr_td_all += '<tr>';
                        for (var j = 0; j < schemaList.length; j++) {
                            table_tr_td_all += ('<td>' + debug_data_list_i_obj[schemaList[j]] + '</td>');
                        }
                        table_tr_td_all += '</tr>';
                    }
                    debug_data_table_id_obj += (table_tr_td_all + '</table>');
                    div_table_list_obj.append(debug_data_table_id_obj);
                }
            }
        }
    });
}

function changePageNo(switchNo) {
    // Get all the tables under div_table_list and hide
    var div_table_list_obj = $('#div_table_list');
    var div_table_list_obj_children = div_table_list_obj.children();
    for (var i = 0; i < div_table_list_obj_children.length; i++) {
        $(div_table_list_obj_children[i]).hide();
    }
    //Find the table to switch
    var debug_data_table_id_current_obj = $("#debug_data_table_id_" + switchNo);
    // Determine whether there is content, there is content to display, otherwise request
    if (debug_data_table_id_current_obj.html()) {
        debug_data_table_id_current_obj.show();
    } else {
        loadDebugData();
    }
    // Retrieve all the tables under div_table_list
    div_table_list_obj = $('#div_table_list');

    // Find the element of the tag page for tag removal and replacement
    var current_page_obj = $('#current_page_id');
    // Whether the element of the tag page exists
    if (current_page_obj.html()) {
        var current_page_obj_em_arr = current_page_obj.find("em");
        for (var i = 0; i < current_page_obj_em_arr.length; i++) {
            var innerHTML_i = current_page_obj_em_arr[1].innerHTML;
            if (innerHTML_i) {
                current_page_obj.before('<a id="page_no_id_' + innerHTML_i + '" '
                    + 'href="javascript:changePageNo(' + innerHTML_i + ');">'
                    + innerHTML_i + '</a>');
                current_page_obj.remove();
                break;
            }
        }
    }
    if (switchNo == 1 && switchNo == div_table_list_obj.children().length && isEnd) {// Determine whether it is both the first page and the last page
        $("#debug_data_prev").attr("href", "javascript:void(0);");
        $("#debug_data_prev").addClass("layui-disabled");
        $("#debug_data_next").addClass("layui-disabled");
        $("#debug_data_next").attr("href", "javascript:void(0);");
    } else if (switchNo == 1) { // Determine if the page to be switched is the home page
        $("#debug_data_prev").attr("href", "javascript:void(0);");
        $("#debug_data_prev").addClass("layui-disabled");
        $("#debug_data_next").removeClass("layui-disabled");
        $("#debug_data_next").attr("href", "javascript:changePageNo(" + (switchNo + 1) + ");");
    } else if (switchNo == div_table_list_obj.children().length && isEnd) { // Determine if the page to be switched is the last page
        $("#debug_data_prev").removeClass("layui-disabled");
        $("#debug_data_prev").attr("href", "javascript:changePageNo(" + (switchNo - 1) + ");");
        $("#debug_data_next").addClass("layui-disabled");
        $("#debug_data_next").attr("href", "javascript:void(0);");
    } else {//When it is neither the first page nor the last page
        $("#debug_data_prev").removeClass("layui-disabled");
        $("#debug_data_prev").attr("href", "javascript:changePageNo(" + (switchNo - 1) + ");");
        $("#debug_data_next").removeClass("layui-disabled");
        $("#debug_data_next").attr("href", "javascript:changePageNo(" + (switchNo + 1) + ");");
    }
    var page_no_switch_obj = $('#page_no_id_' + switchNo);
    var current_page_no_obj = '<span id="current_page_id" class="layui-laypage-curr">'
        + '<em class="layui-laypage-em"></em>'
        + '<em>' + switchNo + '</em></span>';
    if (page_no_switch_obj.html()) {
        page_no_switch_obj.after(current_page_no_obj);
        page_no_switch_obj.remove();
    } else {
        $("#debug_data_next").before(current_page_no_obj);
    }
}