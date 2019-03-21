var processTable;
var isTableLoading = true;

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
        $.ajax({
            cache: true,
            type: "get",
            url: "/piflow-web/process/getAppInfoList",
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
                    if ('0' !== dataMap.code) {
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
function getCheckpointList(id, processId, parentProcessId) {
    if (!processId) {
        alert("Error，Did not get processId");
        return;
    }
    $('#fullScreenList').show();
    $('#checkpointListShow').modal('hide');
    $.ajax({
        cache: true,//保留缓存数据
        type: "POST",//为POST请求
        url: "/piflow-web/process/getCheckpoint",//这是我在后台接受数据的文件名
        //data:$('#loginForm').serialize(),//将该表单序列化
        data: {
            pID: processId,
            parentProcessId: parentProcessId
        },
        async: true,//设置成true，这标志着在请求开始后，其他代码依然能够执行。如果把这个选项设置成false，这意味着所有的请求都不再是异步的了，这也会导致浏览器被锁死
        error: function (request) {//请求失败之后的操作
            alert("Request Failed");
            $('#fullScreenList').hide();
            $('#checkpointListShow').modal('hide');
            return;
        },
        success: function (data) {//请求成功之后的操作
            //console.log("success");
            $('#checkpointListContent').html(data);
            $('#runProcessBtn').attr('onclick', 'listRunProcess(true,"' + id + '")');
            $('#stopProcessBtn').attr('onclick', 'listRunProcess(false,"' + id + '")');
            if ($('#checkpointsIsNull').val()) {
                //alert("No Checkpoint was queried");
                listRunProcess(true, id);
            } else {
                $('#checkpointListShow').modal('show');
            }

        }
    });
}

//运行
function listRunProcess(flag, id) {
    $('#checkpointListShow').modal('hide');
    if (!flag) {
        $('#fullScreenList').hide();
        return;
    }
    var checkpointStr = '';
    $('#checkpointListContent').find("input[type='checkbox']:checked").each(function () {
        if ('' !== checkpointStr) {
            checkpointStr = (checkpointStr + ',');
        }
        checkpointStr = (checkpointStr + $(this).val());
    });
    $('#fullScreenList').show();
    $.ajax({
        cache: true,//保留缓存数据
        type: "POST",//为POST请求
        url: "/piflow-web/process/runProcess",//这是我在后台接受数据的文件名
        //data:$('#loginForm').serialize(),//将该表单序列化
        data: {
            id: id,
            checkpointStr: checkpointStr
        },
        async: true,//设置成true，这标志着在请求开始后，其他代码依然能够执行。如果把这个选项设置成false，这意味着所有的请求都不再是异步的了，这也会导致浏览器被锁死
        error: function (request) {//请求失败之后的操作
            alert("Request Failed");
            $('#fullScreenList').hide();
            return;
        },
        success: function (data) {//请求成功之后的操作
            //console.log("success");
            var dataMap = JSON.parse(data);
            if ('0' !== dataMap.code) {
                //alert(dataMap.errMsg);
                window.location.reload();
                window.open("/piflow-web/process/getProcessById?processId=" + dataMap.processId);
            } else {
                alert("Startup Failed");
                $('#fullScreenList').hide();
            }

        }
    });
}

//停止
function stopProcess(processID) {
    $('#fullScreenList').show();
    $.ajax({
        cache: true,//保留缓存数据
        type: "POST",//为POST请求
        url: "/piflow-web/process/stopProcess",//这是我在后台接受数据的文件名
        //data:$('#loginForm').serialize(),//将该表单序列化
        data: {
            processId: processID
        },
        async: true,//设置成true，这标志着在请求开始后，其他代码依然能够执行。如果把这个选项设置成false，这意味着所有的请求都不再是异步的了，这也会导致浏览器被锁死
        error: function (request) {//请求失败之后的操作
            alert("Request Failed");
            $('#fullScreenList').hide();
            return;
        },
        success: function (data) {//请求成功之后的操作
            //console.log("success");
            var dataMap = JSON.parse(data);
            if ('0' !== dataMap.code) {
                alert(dataMap.errMsg);
                window.location.reload();
            } else {
                alert("Stop Failed:" + dataMap.errMsg);
            }
            $('#fullScreenList').hide();

        }
    });
}

//删除
function delProcess(processID) {
    $('#fullScreenList').show();
    $.ajax({
        cache: true,//保留缓存数据
        type: "get",//为get请求
        url: "/piflow-web/process/delProcess",//这是我在后台接受数据的文件名
        //data:$('#loginForm').serialize(),//将该表单序列化
        data: {
            processID: processID
        },
        async: true,//设置成true，这标志着在请求开始后，其他代码依然能够执行。如果把这个选项设置成false，这意味着所有的请求都不再是异步的了，这也会导致浏览器被锁死
        error: function (request) {//请求失败之后的操作
            alert("Request Failed");
            $('#fullScreenList').hide();
            return;
        },
        success: function (data) {//请求成功之后的操作
            var dataMap = JSON.parse(data);
            if ('0' !== dataMap.code) {
                alert(dataMap.errMsg);
                window.location.reload();
            } else {
                alert("Delete Failed" + dataMap.errMsg);
            }
            $('#fullScreenList').hide();
        }
    });
}

function initDatatablePage(testTableId, url) {
    processTable = $('#' + testTableId).DataTable({
        "pagingType": "full_numbers",//设置分页控件的模式
        "searching": true,//屏蔽datatales的查询框
        "aLengthMenu": [10, 20, 50, 100],//设置一页展示10条记录
        "bAutoWidth": true,
        "bLengthChange": true,//屏蔽tables的一页展示多少条记录的下拉列表
        "ordering": false, // 禁止排序
        "oLanguage": {
            "sSearch": "<span>Filter records:</span> _INPUT_",
            "sLengthMenu": "<span>Show entries:</span> _MENU_",
            "oPaginate": {"sFirst": "First", "sLast": "Last", "sNext": ">", "sPrevious": "<"}
        },
        "processing": true, //打开数据加载时的等待效果
        "serverSide": true,//打开后台分页
        "ajax": {
            "url": url,
            "data": function (d) {
                var level1 = $('#level1').val();
                //添加额外的参数传给服务器
                d.extra_search = d.search.value;
            },
            "dataSrc": responseHandler
        },
        "columns": [
            {"mDataProp": "appId",},
            {"mDataProp": "name"},
            {"mDataProp": "description",},
            {"mDataProp": "startTime",},
            {"mDataProp": "endTime",},
            {"mDataProp": "progress",},
            {"mDataProp": "state",},
            {"mDataProp": "actions",}
        ]

    });
}

//后台返回的结果
function responseHandler(res) {
    let resPageData = res.pageData;
    var pageData = []
    if (resPageData && resPageData.length > 0) {
        for (var i = 0; i < resPageData.length; i++) {
            var data1 = {
                "appId": "<div name='processAppId'></div>",
                "name": "",
                "description": "",
                "startTime": "<div id='" + resPageData[i].id + "startTime' name='processStartTime'></div>",
                "endTime": "<div id='" + resPageData[i].id + "endTime' name='processEndTime'></div>",
                "progress": "",
                "state": "<div name='processState'>No State</div>",
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

                var progressHtmlStr = '<div id="d">' +
                    '<p id="' + resPageData[i].id + 'Info">progress:' +
                    (resPageData[i].progress ? (resPageData[i].progress + '%') : '0%') +
                    '</p>' +
                    '<progress id="' + resPageData[i].id + '" max="100" value="' + resPageData[i].progress + '">' +
                    '</progress>' +
                    '</div>';

                var actionsHtmlStr = '<div style="width: 100%; text-align: center" >' +
                    '<a class="btn" ' +
                    'href="/piflow-web/process/getProcessById?processId=' + resPageData[i].id + '">' +
                    '<i class="icon-share-alt icon-white"></i>' +
                    '</a>&nbsp;' +
                    '<a class="btn" ' +
                    'href="javascript:void(0);" ' +
                    'onclick="javascript:getCheckpointList(\'' + resPageData[i].id + '\',\'' + resPageData[i].parentProcessId + '\',\'null\');">' +
                    '<i class="icon-play icon-white"></i>' +
                    '</a>&nbsp;' +
                    '<a class="btn" ' +
                    'href="javascript:void(0);" ' +
                    'onclick="javascript:stopProcess(\'' + resPageData[i].id + '\');">' +
                    '<i class="icon-stop icon-white"></i>' +
                    '</a>&nbsp;' +
                    '<a class="btn" ' +
                    'href="javascript:void(0);" ' +
                    'onclick="javascript:delProcess(\'' + resPageData[i].id + '\');">' +
                    '<i class="icon-trash icon-white"></i>' +
                    '</a>&nbsp;' +
                    '</div>';
                if (resPageData[i].appId) {
                    data1.appId = '<div name="processAppId" style="word-wrap: break-word;">' + resPageData[i].appId + '</div>';
                }
                if (resPageData[i].name) {
                    data1.name = resPageData[i].name;
                }
                if (resPageData[i].startTime) {
                    data1.startTime = '<div id="' + resPageData[i].id + 'startTime" name="processStartTime" style="word-wrap: break-word;">' +
                        resPageData[i].startTime +
                        '</div>';
                }
                if (resPageData[i].endTime) {
                    data1.endTime = '<div id="' + resPageData[i].id + 'endTime" name="processEndTime" style="word-wrap: break-word;">' +
                        resPageData[i].endTime + '</div>';
                }
                if (resPageData[i].state) {
                    data1.state = '<div id="' + resPageData[i].id + 'state" name="processState" style="word-wrap: break-word;">' +
                        resPageData[i].state.text + '</div>';
                }
                if (descriptionHtmlStr) {
                    data1.description = descriptionHtmlStr;
                }
                if (progressHtmlStr) {
                    data1.progress = progressHtmlStr;
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

function search1() {
    processTable.ajax.reload();
}
