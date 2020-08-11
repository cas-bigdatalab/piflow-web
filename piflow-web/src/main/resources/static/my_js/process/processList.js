var isTableLoading = true;

function initProcessDatatablePage(testTableId, url, searchInputId) {
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
                {
                    field: 'appId', title: 'ProcessId', sort: true, templet: function (data) {
                        return responseFieldHandler('appId', data);
                    }
                },
                {field: 'name', title: 'Name', sort: true},
                {field: 'description', title: 'Description', sort: true},
                {
                    field: 'startTime', title: 'StartTime', sort: true, width: 170, templet: function (data) {
                        return responseFieldHandler('startTime', data);
                    }
                },
                {
                    field: 'endTime', title: 'EndTime', sort: true, width: 170, templet: function (data) {
                        return responseFieldHandler('endTime', data);
                    }
                },
                {
                    field: 'progress', title: 'Progress', sort: true, width: 220, templet: function (data) {
                        return responseFieldHandler('progress', data);
                    }
                },
                {
                    field: 'state', title: 'Status', sort: true, width: 120, templet: function (data) {
                        return responseFieldHandler('state', data);
                    }
                },
                {
                    field: 'right', title: 'Actions', sort: true, width: 240, templet: function (data) {
                        return responseActionHandler(data);
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
}

function searchMonitor(layui_table, layui_table_id, searchInputId) {
    //Perform overload
    layui_table.reload(layui_table_id, {
        page: {
            curr: 1 //Start again on page 1
        }
        , where: {param: $('#' + searchInputId).val()}
    }, 'data');
}

function responseFieldHandler(fileName, data) {
    var responseShowHtml = "";
    switch (fileName) {
        case 'appId': {
            responseShowHtml = ('<div name="processAppId">' + data.appId + '</div>');
            break;
        }
        case 'startTime': {
            data.startTime = data.startTime ? data.startTime : "";
            responseShowHtml = ('<div id="' + data.id + 'startTime" name="processStartTime" >' + data.startTime + '</div>');
            break;
        }
        case 'endTime': {
            data.endTime = data.endTime ? data.endTime : "";
            responseShowHtml = ('<div id="' + data.id + 'endTime" name="processEndTime">' + data.endTime + '</div>');
            break;
        }
        case 'progress': {
            responseShowHtml = '<div>' +
                '<p id="' + data.id + 'Info">' +
                '<progress id="' + data.id + '" max="100" value="' +
                (data.progress ? (data.progress) : '0.00')
                + '">' +
                '</progress> ' +
                (data.progress ? (data.progress + '%') : '0.00%') +
                '</p>' +
                '</div>';
            break;
        }
        case 'state': {
            responseShowHtml = (null != data.state ? data.state.text : '')
            break;
        }

    }
    return responseShowHtml;
}

function responseActionHandler(data) {
    if (!data) {
        return "";
    }
    data.parentProcessId = (data.parentProcessId ? data.parentProcessId : '');
    var openProcessBtn = '<a class="btn" ' +
        'href="javascript:void(0);" ' +
        'onclick="javascript:openProcess(\'' + data.id + '\');" ' +
        'style="margin-right: 2px;">' +
        '<i class="icon-share-alt icon-white"></i>' +
        '</a>';
    var runProcessBtn = '<a class="btn" ' +
        'href="javascript:void(0);" ' +
        'onclick="javascript:getCheckpointList(\'' + data.id + '\',\'' + data.appId + '\',\'' + data.parentProcessId + '\');"' +
        'style="margin-right: 2px;">' +
        '<i class="icon-play icon-white"></i>' +
        '</a>';
    var debugProcessBtn = '<a class="btn" ' +
        'href="javascript:void(0);" ' +
        'onclick="javascript:getCheckpointList(\'' + data.id + '\',\'' + data.appId + '\',\'' + data.parentProcessId + ',\'DEBUG\');"' +
        'style="margin-right: 2px;">' +
        '<i class="fa-bug icon-white"></i>' +
        '</a>';
    var stopProcessBtn = '<a class="btn" ' +
        'href="javascript:void(0);" ' +
        'onclick="javascript:stopProcess(\'' + data.id + '\');"' +
        'style="margin-right: 2px;">' +
        '<i class="icon-stop icon-white"></i>' +
        '</a>';
    var delProcessBtn = '<a class="btn" ' +
        'href="javascript:void(0);" ' +
        'onclick="javascript:delProcess(\'' + data.id + '\');"' +
        'style="margin-right: 2px;">' +
        '<i class="icon-trash icon-white"></i>' +
        '</a>';
    return '<p style="width: 100%; text-align: center" >' + openProcessBtn + runProcessBtn + debugProcessBtn + stopProcessBtn + delProcessBtn + '</p>';
}

function processListMonitoring() {
    var arrayObj = new Array();
    var processAppIds = $("div[name='processAppId']");
    var processStartTimes = $("div[name='processStartTime']");
    var processStates = $("div[name='processState']");
    if (processAppIds && processStartTimes && processStates) {
        if (processAppIds.length == processStartTimes.length && processStartTimes.length === processStates.length) {
            for (var i = 0; i < processAppIds.length; i++) {
                if (processAppIds[i].innerHTML != "") {
                    if (processStates[i].innerHTML != "No state" && processStates[i].innerHTML == "STARTED") {
                        arrayObj.push(processAppIds[i].innerHTML);
                    }
                }
                if (processStartTimes[i].innerHTML == '') {
                    arrayObj.push(processAppIds[i].innerHTML);
                }
            }
            if (processAppIds.length > 0) {
                isTableLoading = false;
            }
        }
    } else {
        isTableLoading = true;
    }
    if (isTableLoading) {
        return;
    } else {
        if (arrayObj.length == 0) {
            window.clearInterval(timer);
            return;
        }
        ajaxRequest({
            cache: true,
            type: "get",
            url: "/process/getAppInfoList",
            data: {arrayObj: arrayObj},
            async: true,
            traditional: true,
            error: function (request) {
                console.log("error");
                return;
            },
            success: function (data) {
                if (null != data) {
                    var dataMap = JSON.parse(data);
                    if (200 === dataMap.code) {
                        if (arrayObj && arrayObj.length > 0) {
                            for (var i in arrayObj) {
                                var strAppID = arrayObj[i];
                                if (strAppID && '' !== strAppID) {
                                    var process = dataMap[strAppID];
                                    if (process && '' !== process) {
                                        if (process.id && '' != process.id) {
                                            document.getElementById("" + process.id + "").value = process.progress;
                                            document.getElementById("" + process.id + "Info").innerHTML = "progress:" + process.progress + "%";
                                            if (process.state && "" !== process.state) {
                                                document.getElementById("" + process.id + "state").innerHTML = process.state.text;
                                            }
                                            if (dataMap.startTime && "" !== process.startTime) {
                                                document.getElementById("" + process.id + "startTime").innerHTML = process.startTime;
                                            }
                                            if (dataMap.endTime && "" !== process.endTime) {
                                                document.getElementById("" + process.id + "endTime").innerHTML = process.endTime;
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
        });
    }
}

//获取Checkpoint点
function getCheckpointList(id, processId, parentProcessId, runMode) {
    if (!processId) {
        alert("Error，Did not get processId");
        return;
    }
    $('#fullScreen').show();
    ajaxRequest({
        cache: true,//Keep cached data
        type: "get",//Request for get
        url: "/process/getCheckpointData",//This is the name of the file where I receive data in the background.
        //data:$('#loginForm').serialize(),//Serialize the form
        data: {
            pID: pID,
            parentProcessId: parentProcessId,
        },
        async: false,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            alert("Request Failed");
            return;
        },
        success: function (data) {//Operation after request successful
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                var checkpointsSplitArray = dataMap.checkpointsSplit;
                if (checkpointsSplitArray) {
                    var layer_open_checkpoint_top = document.createElement("div");
                    var layer_open_checkpoint_btn_div = document.createElement("div");
                    layer_open_checkpoint_btn_div.setAttribute("style", "text-align: right;");

                    var layer_open_checkpoint_btn = document.createElement("div");
                    layer_open_checkpoint_btn.type = "button";
                    layer_open_checkpoint_btn.className = "btn btn-default";
                    layer_open_checkpoint_btn.setAttribute("style", "margin-right: 10px;");
                    if (runMode && 'DEBUG' === runMode) {
                        layer_open_checkpoint_btn.textContent = "DEBUG";
                        layer_open_checkpoint_btn.setAttribute('onclick', 'listRunProcess("' + id + '","DEBUG")')
                    } else {
                        layer_open_checkpoint_btn.setAttribute('onclick', 'listRunProcess("' + id + '")');
                        layer_open_checkpoint_btn.textContent = "RUN"
                    }
                    layer_open_checkpoint_btn_div.appendChild(layer_open_checkpoint_btn);

                    var layer_open_checkpoint = document.createElement("div");
                    layer_open_checkpoint.id = "checkpointsContentDiv";

                    for (var i = 0; i < checkpointsSplitArray.length; i++) {
                        var checkpoints_content_span = document.createElement("span");

                        var checkpoints_content_span_input = document.createElement("input");
                        checkpoints_content_span_input.type = "checkbox";
                        checkpoints_content_span_input.value = "'" + checkpointsSplitArray[i] + "'";

                        var checkpoints_content_span_span = document.createElement("span");
                        checkpoints_content_span_span.textContent = checkpointsSplitArray[i];

                        var checkpoints_content_span_br = document.createElement("br");

                        checkpoints_content_span.appendChild(checkpoints_content_span_input);
                        checkpoints_content_span.appendChild(checkpoints_content_span_span);
                        checkpoints_content_span.appendChild(checkpoints_content_span_br);

                        layer_open_checkpoint.appendChild(checkpoints_content_span);

                    }

                    layer_open_checkpoint_top.appendChild(layer_open_checkpoint);
                    layer_open_checkpoint_top.appendChild(layer_open_checkpoint_btn_div);

                    openLayerWindowLoadHtml(layer_open_checkpoint_top.outerHTML, 500, 300, "Checkpoint", 0.3);
                    //$('#fullScreen').hide();
                } else {
                    listRunProcess(processId, runMode);
                }
            }
        }
    });
}

function cancelListRunProcess() {
    $('#checkpointListShow').modal('hide');
    $('#fullScreen').hide();
    return;
}

//run
function listRunProcess(id, runMode) {
    $('#checkpointListShow').modal('hide');
    var checkpointStr = '';
    $('#checkpointListContent').find("input[type='checkbox']:checked").each(function () {
        if ('' !== checkpointStr) {
            checkpointStr = (checkpointStr + ',');
        }
        checkpointStr = (checkpointStr + $(this).val());
    });
    $('#fullScreen').show();
    var data = {
        id: id,
        checkpointStr: checkpointStr
    }
    if (runMode) {
        data.runMode = runMode;
    }
    ajaxRequest({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/process/runProcess",//This is the name of the file where I receive data in the background.
        //data:$('#loginForm').serialize(),//Serialize the form
        data: data,
        async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            alert("Request Failed");
            $('#fullScreen').hide();
            return;
        },
        success: function (data) {//Operation after request successful
            //console.log("success");
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                //alert(dataMap.errorMsg);
                //window.location.reload();
                var windowOpen = window.open("/piflow-web/mxGraph/drawingBoard?drawingBoardType=PROCESS&processType=PROCESS&load=" + dataMap.processId);
                if (windowOpen == null || typeof (windowOpen) == 'undefined') {
                    alert('The window cannot be opened. Please check your browser settings.')
                }
            } else {
                alert("Startup Failed");
                $('#fullScreen').hide();
            }

        }
    });
}

//stop
function stopProcess(processID) {
    $('#fullScreen').show();
    ajaxRequest({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/process/stopProcess",//This is the name of the file where I receive data in the background.
        //data:$('#loginForm').serialize(),//Serialize the form
        data: {
            processId: processID
        },
        async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            alert("Request Failed");
            $('#fullScreen').hide();
            return;
        },
        success: function (data) {//Operation after request successful
            //console.log("success");
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                alert(dataMap.errorMsg);
                window.location.reload();
            } else {
                alert("Stop Failed:" + dataMap.errorMsg);
            }
            $('#fullScreen').hide();

        }
    });
}

//del
function delProcess(processID) {
    $('#fullScreen').show();
    ajaxRequest({
        cache: true,//Keep cached data
        type: "get",//Request for get
        url: "/process/delProcess",//This is the name of the file where I receive data in the background.
        //data:$('#loginForm').serialize(),//Serialize the form
        data: {
            processID: processID
        },
        async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            alert("Request Failed");
            $('#fullScreen').hide();
            return;
        },
        success: function (data) {//Operation after request successful
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                alert(dataMap.errorMsg);
                window.location.reload();
            } else {
                alert("Delete Failed" + dataMap.errorMsg);
            }
            $('#fullScreen').hide();
        }
    });
}

// open
function openProcess(loadId) {
    var windowOpen = window.open('/piflow-web/mxGraph/drawingBoard?drawingBoardType=PROCESS&processType=PROCESS&load=' + loadId);
    if (windowOpen == null || typeof (windowOpen) == 'undefined') {
        alert('The window cannot be opened. Please check your browser settings.');
    }
}
