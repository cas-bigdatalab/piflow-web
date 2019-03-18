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
        area: ['580px', '520px'], //宽高
        skin: 'layui-layer-rim', //加上边框
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
        area: ['580px', '520px'], //宽高
        skin: 'layui-layer-rim', //加上边框
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
            cache: true,//保留缓存数据
            type: "get",//为post请求
            url: "/piflow-web/flow/saveFlowInfo",//这是我在后台接受数据的文件名
            data: {
                name: flowName,
                description: description,
                driverMemory: driverMemory,
                executorNumber: executorNumber,
                executorMemory: executorMemory,
                executorCores: executorCores
            },
            async: false,//设置成true，这标志着在请求开始后，其他代码依然能够执行。如果把这个选项设置成false，这意味着所有的请求都不再是异步的了，这也会导致浏览器被锁死
            error: function (request) {//请求失败之后的操作
                layer.closeAll('page');
                layer.msg('creation failed ', {icon: 2, shade: 0, time: 2000}, function () {
                });
                return;
            },
            success: function (data) {//请求成功之后的操作
                layer.closeAll('page');
                var dataMap = JSON.parse(data);
                if ("1" === dataMap.code) {
                    layer.msg('create success ', {icon: 1, shade: 0, time: 2000}, function () {
                        var tempwindow = window.open('_blank');
                        tempwindow.location = "/piflow-web/grapheditor/home?load=" + dataMap.flowId;
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
            cache: true,//保留缓存数据
            type: "get",//为post请求
            url: "/piflow-web/flow/updateFlowInfo",//这是我在后台接受数据的文件名
            data: {
                id: id,
                name: flowName,
                description: description,
                driverMemory: driverMemory,
                executorNumber: executorNumber,
                executorMemory: executorMemory,
                executorCores: executorCores
            },
            async: true,//设置成true，这标志着在请求开始后，其他代码依然能够执行。如果把这个选项设置成false，这意味着所有的请求都不再是异步的了，这也会导致浏览器被锁死
            error: function (request) {//请求失败之后的操作
                layer.closeAll('page');
                layer.msg('update failed ', {icon: 2, shade: 0, time: 2000}, function () {
                    window.location.reload();
                });
                return;
            },
            success: function (data) {//请求成功之后的操作
                if (data > 0) {
                    layer.closeAll('page');
                    layer.msg('update success', {icon: 1, shade: 0, time: 2000}, function () {
                        location.reload();
                    });
                }
            }
        });
}

//运行
function runFlows(loadId) {
    $('#fullScreenList').show();
    $.ajax({
        cache: true,//保留缓存数据
        type: "get",//为post请求
        url: "/piflow-web/flow/runFlow",//这是我在后台接受数据的文件名
        //data:$('#loginForm').serialize(),//将该表单序列化
        data: {flowId: loadId},
        async: true,//设置成true，这标志着在请求开始后，其他代码依然能够执行。如果把这个选项设置成false，这意味着所有的请求都不再是异步的了，这也会导致浏览器被锁死
        error: function (request) {//请求失败之后的操作
            $('#fullScreenList').hide();
            //alert("reload fail");
            layer.msg("reload fail", {icon: 2, shade: 0, time: 2000}, function () {});
            return;
        },
        success: function (data) {//请求成功之后的操作
            var dataMap = JSON.parse(data);
            if ('0' !== dataMap.code) {
                $('#fullScreenList').hide();
                layer.msg(dataMap.errMsg, {icon: 1, shade: 0, time: 2000}, function () {
                    //启动成功后跳转至监控页面
                    var tempwindow = window.open('_blank');
                    tempwindow.location = "/piflow-web/process/getProcessById?processId=" + dataMap.processId;
                });
            } else {
                $('#fullScreenList').hide();
                layer.msg(dataMap.errMsg, {icon: 2, shade: 0, time: 2000}, function () {
                    location.reload();
                });
            }

        }
    });
}

function tableRow() {
    var arrayObj = new Array();
    var table = $("table tr").find("td:eq(1)");
    var state = $("table tr").find("td:eq(7)");
    for (var i = 0; i < table.length; i++) {
        if (table[i].innerHTML != "") {
            if (state[i].innerHTML != "No state" && state[i].innerHTML == "STARTED") {
                arrayObj.push(table[i].innerHTML);
            }
        }
    }
    if (arrayObj.length == 0) {
        clearInterval(c);
        return;
    }
    $.ajax({
        cache: true,
        type: "get",
        url: "/piflow-web/flowInfoDb/list",
        data: {content: arrayObj},
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
                    document.getElementById("" + dataMap.id + "").value = dataMap.progress;
                    document.getElementById("" + dataMap.id + "Info").innerHTML = "进度" + dataMap.progress + "%";
                    document.getElementById("" + dataMap.id + "state").innerHTML = dataMap.state;
                    if (dataMap.endTime && "" !== dataMap.startTime) {
                        document.getElementById("" + dataMap.id + "startTime").innerHTML = dataMap.startTime;
                        document.getElementById("" + dataMap.id + "endTime").innerHTML = dataMap.endTime;
                    }
                }
            }
        }
    });
}

function deleteFlow(id, name) {
    layer.confirm("Are you sure to delete '" + name + "' ?", {
        btn: ['confirm', 'cancel'] //按钮
        , title: 'Confirmation prompt'
    }, function () {
        $.ajax({
            cache: true,//保留缓存数据
            type: "get",//为post请求
            url: "/piflow-web/flow/deleteFlow",//这是我在后台接受数据的文件名
            data: {id: id},
            async: true,//设置成true，这标志着在请求开始后，其他代码依然能够执行。如果把这个选项设置成false，这意味着所有的请求都不再是异步的了，这也会导致浏览器被锁死
            error: function (request) {//请求失败之后的操作
                return;
            },
            success: function (data) {//请求成功之后的操作
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
            cache: true,//保留缓存数据
            type: "get",//为post请求
            url: "/piflow-web/template/saveTemplate",//这是我在后台接受数据的文件名
            data: {load: id, name: text},
            async: true,
            error: function (request) {//请求失败之后的操作
                console.log(" save template error");
                return;
            },
            success: function (data) {//请求成功之后的操作
                var dataMap = JSON.parse(data);
                if (0 !== dataMap.code) {
                    layer.msg(dataMap.errMsg, {icon: 1, shade: 0, time: 2000}, function () {
                    });
                } else {
                    layer.msg(dataMap.errMsg, {icon: 2, shade: 0, time: 2000}, function () {
                    });
                }
            }
        });
    });
}

function initDatatableFlowPage(testTableId, url) {
    flowTable = $('#' + testTableId).DataTable({
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
            "dataSrc": responseHandlerFlow
        },
        "columns": [
            {"mDataProp": "name"},
            {"mDataProp": "description"},
            {"mDataProp": "crtDttm"},
            {"mDataProp": "actions",'sClass': "text-center"}
        ]

    });
}

//后台返回的结果
function responseHandlerFlow(res) {
    let resPageData = res.pageData;
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
                    'href="/piflow-web/grapheditor/home?load=' + resPageData[i].id + '"' +
                    'target="_blank" >' +
                    '<i class="icon-share-alt icon-white"></i>' +
                    '</a>&nbsp;' +
                    '<a class="btn" ' +
                    'href="javascript:void(0);" ' +
                    'onclick="javascript:update(\'' +
                    resPageData[i].id + '\',\'' +
                    resPageData[i].name + '\',\'' +
                    resPageData[i].description + '\',\'' +
                    resPageData[i].driverMemory + '\',\'' +
                    resPageData[i].executorNumber + '\',\'' +
                    resPageData[i].executorMemory + '\',\'' +
                    resPageData[i].executorCores + '\');">' +
                    '<i class="icon-edit icon-white"></i>' +
                    '</a>&nbsp;' +
                    '<a class="btn" ' +
                    'href="javascript:void(0);" ' +
                    'onclick="javascript:runFlows(\'' + resPageData[i].id + '\');">' +
                    '<i class="icon-play icon-white"></i>' +
                    '</a>&nbsp;' +
                    '<a class="btn" ' +
                    'href="javascript:void(0);" ' +
                    'onclick="javascript:deleteFlow(\'' +
                    resPageData[i].id + '\',\'' +
                    resPageData[i].name + '\');">' +
                    '<i class="icon-trash icon-white"></i>' +
                    '</a>&nbsp;' +
                    '<a class="btn" ' +
                    'href="javascript:void(0);" ' +
                    'onclick="javascript:saveTableTemplate(\'' +
                    resPageData[i].id + '\',\'' +
                    resPageData[i].name + '\');">' +
                    '<i class="icon-check icon-white"></i>' +
                    '</a>&nbsp;' +
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
    var fullScreenList = $('#fullScreenList');
    fullScreenList.show();
    $.ajax({
        data: {"load": ''},
        cache: true,//保留缓存数据
        type: "POST",//为post请求
        url: "/piflow-web/grapheditor/reloadStops",//这是我在后台接受数据的文件名
        error: function (request) {//请求失败之后的操作
            //fullScreenList.hide();
            //alert("reload fail");
            layer.msg("reload fail", {icon: 2, shade: 0, time: 2000}, function () {});
            return;
        },
        success: function (data) {//请求成功之后的操作
            var dataMap = JSON.parse(data);
            if (0 !== dataMap.code) {
                //alert("reload success");
                layer.msg("reload success", {icon: 1, shade: 0, time: 2000}, function () {});
                fullScreenList.hide();
            } else {
                //alert("reload fail");
                layer.msg("reload fail", {icon: 2, shade: 0, time: 2000}, function () {});
                fullScreenList.hide();
            }
        }
    });
}