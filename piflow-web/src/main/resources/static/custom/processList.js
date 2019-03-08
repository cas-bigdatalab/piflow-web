function processListMonitoring() {
    var arrayObj = new Array();
    var processAppIds = $("td[name='processAppId']");
    var processStartTimes = $("td[name='processStartTime']");
    var processStates = $("td[name='processState']");
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
        }
    }
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
                alert("No Checkpoint was queried");
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
                alert(dataMap.errMsg);
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

function initTable(tableId, url, columns) {
    $('#' + tableId).bootstrapTable('destroy');
    $('#' + tableId).bootstrapTable({
        url: url,
        method: 'post',
        toolbar: '#toolbar',        //工具按钮用哪个容器
        pagination: true,                   //是否显示分页（*）
        contentType: "application/json",
        sortable: false,
        queryParams: queryTableParams,
        cache: false,
        singleSelect: true,                         //单选
        clickToSelect: true,
        showRefresh: false,                  //是否显示刷新按钮
        showPaginationSwitch: true,       //是否显示选择分页数按钮
        pageNumber: 1,                       //初始化加载第一页，默认第一页
        pageSize: 10, //每页的记录行数（*）
        search: false,
        sidePagination: "server",   //分页方式：client客户端分页，server服务端分页（*）
        columns: columns,

    });
    // 隐藏主键显示
    //$('#' + tableId).bootstrapTable('hideColumn', 'innerId');
}

function queryTableParams(params) {
    var temp = {
        pageNumber: params.limit + params.offset - 1,
        pageSize: params.offset
    }
    return JSON.stringify(temp);
}


function TableInit(tableId, url, columns) {
    //初始化Table
    $('#' + tableId).bootstrapTable({

        url: url,         //请求后台的URL（*）
        method: 'POST',                      //请求方式（*）
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        sortOrder: "asc",                   //排序方式
        queryParamsType: '',
        dataType: 'json',
        paginationShowPageGo: true,
        showJumpto: true,
        pageNumber: 1, //初始化加载第一页，默认第一页
        queryParams: queryParams,//请求服务器时所传的参数
        sidePagination: 'server',//指定服务器端分页
        pageSize: 10,//单页记录数
        pageList: [10, 20, 30, 40],//分页步进值
        search: false, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        silent: true,
        showRefresh: false,                  //是否显示刷新按钮
        showToggle: false,
        minimumCountColumns: 2,             //最少允许的列数
        uniqueId: "id",                     //每一行的唯一标识，一般为主键列

        columns: [
            /*{
                field: 'index',
                title: 'No',
                align: 'center'
            },*/
            {
                field: 'appId',
                title: 'ProcessID',
                align: 'center',
                width: '230px'
            },
            {
                field: 'name',
                title: 'Name',
                align: 'center',
                width: '230px'
            },
            {
                field: 'description',
                title: 'Description',
                align: 'center'
            }, {
                field: 'startTime',
                title: 'StartTime',
                align: 'center'
            }, {
                field: 'endTime',
                title: 'EndTime',
                align: 'center'
            },
            {
                field: 'progress',
                title: 'Progress',
                align: 'center'
            }, {
                field: 'state',
                title: 'State',
                align: 'center'
            }, {
                field: 'operation',
                title: 'Actions',
                align: 'center',
                formatter: addFunctionAlty//表格中增加按钮
            }],
        responseHandler: function (res) {  //后台返回的结果
            console.log(res);
            if (res.code == 200) {
                var userInfo = res.rowDatas;
                var NewData = [];
                if (userInfo.length) {
                    for (var i = 0; i < userInfo.length; i++) {
                        var dataNewObj = {
                            'id': '',
                            'parentProcessId': '',
                            'appId': '',
                            "name": '',
                            'description': '',
                            "startTime": '',
                            'endTime': '',
                            'progress': '',
                            'state': ''
                        };

                        dataNewObj.id = userInfo[i].id;
                        dataNewObj.parentProcessId = userInfo[i].parentProcessId;
                        dataNewObj.appId = userInfo[i].appId;
                        dataNewObj.name = userInfo[i].name;
                        dataNewObj.description = userInfo[i].description;
                        dataNewObj.startTime = userInfo[i].startTime;
                        dataNewObj.endTime = userInfo[i].endTime;
                        dataNewObj.progress = userInfo[i].progress;
                        dataNewObj.state = userInfo[i].state;
                        NewData.push(dataNewObj);
                    }
                    console.log(NewData)
                }
                var data = {
                    total: res.total,
                    rows: NewData
                };

                return data;
            }

        }
    });

    // 得到查询的参数
    function queryParams(params) {
        var userName = $("#keyWord").val();
        console.log(userName);
        var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageNum: params.pageNumber,
            pageSize: params.pageSize,
            username: userName
        };
        return JSON.stringify(temp);
    }
    //$('#' + tableId).bootstrapTable('showColumn', 'ShopName');
    //$('#' + tableId).bootstrapTable('hideColumn', 'index');
}


// 表格中按钮
function addFunctionAlty(value, row, index) {
    var btnText = '';

    btnText += "<button type=\"button\" id=\"btn_look\" onclick=\"resetPassword(" + "'" + row.id + "'" + ")\" style='width: 77px;' class=\"btn btn-default-g ajax-link\">重置密码</button>  ";

    btnText += "<button type=\"button\" id=\"btn_look\" onclick=\"openCreateUserPage(" + "'" + row.id + "'" + "," + "'编辑')\" class=\"btn btn-default-g ajax-link\">编辑</button>  ";

    btnText += "<button type=\"button\" id=\"btn_stop" + row.id + "\" onclick=\"changeStatus(" + "'" + row.id + "'" + ")\" class=\"btn btn-danger-g ajax-link\">关闭</button>  ";

    btnText += "<button type=\"button\" id=\"btn_stop" + row.id + "\" onclick=\"deleteUser(" + "'" + row.id + "'" + ")\" class=\"btn btn-danger-g ajax-link\">删除</button>  ";

    return btnText;
}


//刷新表格
function getUserList() {
    $("#userTable").bootstrapTable('refresh');
}