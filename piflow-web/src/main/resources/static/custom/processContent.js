var fullScreen = $('#fullScreen');
var startFlow = $('#startFlow');
var stopFlow = $('#stopFlow');
var processContent = $('#processContent');
var checkpointShow = $('#checkpointShow');
var isLoadProcessInfo = true;

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
        //alert("必要位置参数没有获得");
        layer.msg("Necessary position parameters were not obtained", {icon: 2, shade: 0, time: 2000}, function () {
        });
    }
}

// 查询process的基本信息
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
                cache: true,//保留缓存数据
                type: "POST",//为post请求
                url: "/piflow-web/process/queryProcess",//这是我在后台接受数据的文件名
                //data:$('#loginForm').serialize(),//将该表单序列化
                data: {
                    processId: processId
                },
                async: true,//设置成true，这标志着在请求开始后，其他代码依然能够执行。如果把这个选项设置成false，这意味着所有的请求都不再是异步的了，这也会导致浏览器被锁死
                error: function (request) {//请求失败之后的操作
                    console.log("fail");
                    return;
                },
                success: function (data) {//请求成功之后的操作
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

//查询stops的基本信息
function queryProcessStop(processId, pageId) {
    $.ajax({
        cache: true,//保留缓存数据
        type: "POST",//为post请求
        url: "/piflow-web/process/queryProcessStop",//这是我在后台接受数据的文件名
        //data:$('#loginForm').serialize(),//将该表单序列化
        data: {
            processId: processId,
            pageId: pageId
        },
        async: true,//设置成true，这标志着在请求开始后，其他代码依然能够执行。如果把这个选项设置成false，这意味着所有的请求都不再是异步的了，这也会导致浏览器被锁死
        error: function (request) {//请求失败之后的操作
            console.log("fail");
            return;
        },
        success: function (data) {//请求成功之后的操作
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

//查询path基本信息
function queryProcessPath(processId, pageId) {
    if (isLoadProcessInfo) {
        isLoadProcessInfo = false;
    }
    $.ajax({
        cache: true,//保留缓存数据
        type: "POST",//为post请求
        url: "/piflow-web/process/queryProcessPath",//这是我在后台接受数据的文件名
        //data:$('#loginForm').serialize(),//将该表单序列化
        data: {
            processId: processId,
            pageId: pageId
        },
        async: true,//设置成true，这标志着在请求开始后，其他代码依然能够执行。如果把这个选项设置成false，这意味着所有的请求都不再是异步的了，这也会导致浏览器被锁死
        error: function (request) {//请求失败之后的操作
            console.log("fail");
            return;
        },
        success: function (data) {//请求成功之后的操作
            // console.log(data);
            $('#processLeft').html(data);
            toUpperCase();
            $('#selectedArrowShow').show();
            $('#selectedPathShow').show();
            $('#selectedRectShow').hide();
        }
    });
}

//获取Checkpoint点
function getCheckpoint() {
    fullScreen.show();
    $.ajax({
        cache: true,//保留缓存数据
        type: "POST",//为post请求
        url: "/piflow-web/process/getCheckpoint",//这是我在后台接受数据的文件名
        //data:$('#loginForm').serialize(),//将该表单序列化
        data: {
            pID: pID,
            parentProcessId: parentProcessId
        },
        async: true,//设置成true，这标志着在请求开始后，其他代码依然能够执行。如果把这个选项设置成false，这意味着所有的请求都不再是异步的了，这也会导致浏览器被锁死
        error: function (request) {//请求失败之后的操作
            startFlow.show();
            //alert("Request Failed");
            layer.msg("Request Failed", {icon: 2, shade: 0, time: 2000}, function () {
            });
            fullScreen.hide();
            checkpointShow.modal('hide');
            return;
        },
        success: function (data) {//请求成功之后的操作
            //console.log("success");
            $('#checkpointContent').html(data);
            if ($('#checkpointsIsNull').val()) {
                //alert("No Checkpoint was queried");
                layer.msg("No Checkpoint was queried", {icon: 2, shade: 0, time: 2000}, function () {
                });
                //checkpointShow.modal('hide');
                runProcess(true);
            } else {
                checkpointShow.modal('show');
                startFlow.show();
                fullScreen.hide();
            }
        }
    });

}

//运行
function runProcess(flag) {
    fullScreen.show();
    checkpointShow.modal('hide');
    if (!flag) {
        fullScreen.hide();
        return;
    }
    startFlow.hide();
    var checkpointStr = '';
    $('#checkpointContent').find("input[type='checkbox']:checked").each(function () {
        if ('' !== checkpointStr) {
            checkpointStr = (checkpointStr + ',');
        }
        checkpointStr = (checkpointStr + $(this).val());
    });
    $.ajax({
        cache: true,//保留缓存数据
        type: "POST",//为post请求
        url: "/piflow-web/process/runProcess",//这是我在后台接受数据的文件名
        //data:$('#loginForm').serialize(),//将该表单序列化
        data: {
            id: processId,
            checkpointStr: checkpointStr
        },
        async: true,//设置成true，这标志着在请求开始后，其他代码依然能够执行。如果把这个选项设置成false，这意味着所有的请求都不再是异步的了，这也会导致浏览器被锁死
        error: function (request) {//请求失败之后的操作
            startFlow.show();
            //alert("Request Failed");
            layer.msg("Request Failed", {icon: 2, shade: 0, time: 2000}, function () {
            });
            fullScreen.hide();
            return;
        },
        success: function (data) {//请求成功之后的操作
            //console.log("success");
            var dataMap = JSON.parse(data);
            if ('0' !== dataMap.code) {
                //alert(dataMap.errMsg);
                layer.msg(dataMap.errMsg, {icon: 1, shade: 0, time: 2000}, function () {
                });
                window.location.href = "/piflow-web/process/getProcessById?processId=" + dataMap.processId;
            } else {
                //alert(dataMap.errMsg);
                layer.msg(dataMap.errMsg, {icon: 2, shade: 0, time: 2000}, function () {
                });
                startFlow.show();
                fullScreen.hide();
            }

        }
    });
}

//停止
function stopProcess() {
    stopFlow.hide();
    fullScreen.show();
    $.ajax({
        cache: true,//保留缓存数据
        type: "POST",//为post请求
        url: "/piflow-web/process/stopProcess",//这是我在后台接受数据的文件名
        //data:$('#loginForm').serialize(),//将该表单序列化
        data: {
            processId: processId
        },
        async: true,//设置成true，这标志着在请求开始后，其他代码依然能够执行。如果把这个选项设置成false，这意味着所有的请求都不再是异步的了，这也会导致浏览器被锁死
        error: function (request) {//请求失败之后的操作
            stopFlow.show();
            //alert("Request Failed");
            layer.msg("Request Failed", {icon: 2, shade: 0, time: 2000}, function () {
            });
            fullScreen.hide();
            return;
        },
        success: function (data) {//请求成功之后的操作
            //console.log("success");
            var dataMap = JSON.parse(data);
            if ('0' !== dataMap.code) {
                //alert(dataMap.errMsg);
                layer.msg(dataMap.errMsg, {icon: 1, shade: 0, time: 2000}, function () {
                });
                startFlow.show();
            } else {
                //alert("Stop Failed:" + dataMap.errMsg);
                layer.msg("Stop Failed", {icon: 2, shade: 0, time: 2000}, function () {
                });
                stopFlow.show();
            }
            fullScreen.hide();
        }
    });
}

function getLogUrl() {

    $.ajax({
        cache: true,//保留缓存数据
        type: "POST",//为post请求
        url: "/piflow-web/process/getLogUrl",//这是我在后台接受数据的文件名
        //data:$('#loginForm').serialize(),//将该表单序列化
        data: {"appId": appId},
        async: true,//设置成true，这标志着在请求开始后，其他代码依然能够执行。如果把这个选项设置成false，这意味着所有的请求都不再是异步的了，这也会导致浏览器被锁死
        error: function (request) {//请求失败之后的操作
            layer.msg("Request Failed", {icon: 2, shade: 0, time: 2000}, function () {
            });
            return;
        },
        success: function (data) {//请求成功之后的操作
            //console.log("success");
            //alert(data);
            if ('0' !== data.code) {
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
    var window_width = $(window).width();//获取浏览器窗口宽度
    var window_height = $(window).height();//获取浏览器窗口宽度
    $.ajax({
        cache: true,//保留缓存数据
        type: "POST",//为post请求
        url: "/piflow-web/process/getLog",//这是我在后台接受数据的文件名
        //data:$('#loginForm').serialize(),//将该表单序列化
        data: {url: url},
        async: true,//设置成true，这标志着在请求开始后，其他代码依然能够执行。如果把这个选项设置成false，这意味着所有的请求都不再是异步的了，这也会导致浏览器被锁死
        error: function (request) {//请求失败之后的操作
            return;
        },
        success: function (data) {//请求成功之后的操作
            console.log("success");
            if ('' !== data) {
                $('#modalDialog').height(window_height - 124);
                $('#modalDialog').width(window_width - 124);
                var oo = $(data);
                var ff = oo.find('td');
                var pp = '';
                for (var i = 0; i < ff.length; i++) {
                    if ($(ff[i]).attr('class') === 'content') {
                        pp = $(ff[i]).find('pre');
                        break;
                    }
                }
                var tt = '';
                if ('' !== pp) {
                    for (var i = 0; i < pp.length; i++) {
                        if (pp[i]) {
                            tt += $(pp[i]).text();
                        }
                    }
                }
                var pre_height = window_height - 300;
                tt = $(tt).html();
                var ttHtml = '<pre id="preId" style="white-space: pre-wrap; height: ' + pre_height + 'px;">' + tt + '</pre>';
                var htmlSave = "<div id='divPreId' style='height: " + pre_height + "px'>" + ttHtml + "</div>";
                document.getElementById("customContent").innerHTML = htmlSave;
            }
            //alert(data);
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
            if ("0" !== dataMap.code) {
                if (dataMap.state && "" !== dataMap.state) {
                    if ('STARTED' !== dataMap.state) {
                        window.clearInterval(timer);
                        startFlow.show();
                        stopFlow.hide();
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
                                var sotpNameVal = $("#sotpNameShow").text();
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
