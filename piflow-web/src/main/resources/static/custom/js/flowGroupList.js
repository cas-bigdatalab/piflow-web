var flowGroupTable;

function newFlowGroup() {
    $("#buttonFlowGroup").attr("onclick", "");
    $("#buttonFlowGroup").attr("onclick", "saveOrUpdateFlowGroup()");
    $("#flowGroupId").val("");
    $("#flowGroupName").val("");
    $("#description").val("");
    layer.open({
        type: 1,
        title: '<span style="color: #269252;">create flow group</span>',
        shadeClose: true,
        closeBtn: 1,
        shift: 7,
        area: ['580px', '520px'], //Width height
        skin: 'layui-layer-rim', //Add borders
        content: $("#SubmitPage")
    });
}

function initDatatableFlowGroupPage(testTableId, url) {
    flowGroupTable = $('#' + testTableId).DataTable({
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
    console.log(resPageData);
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
                    'href="javascript:void(0);"' +
                    'onclick="javascript:openFlowGroup(\'' + resPageData[i].id + '\'); "' +
                    'style="margin-right: 2px;">' +
                    '<i class="icon-share-alt icon-white"></i>' +
                    '</a>' +
                    '<a class="btn" ' +
                    'href="javascript:void(0);"' +
                    'onclick="javascript:update(\'' + resPageData[i].id + '\',\'' + resPageData[i].name + '\',\'' + resPageData[i].description + '\');" ' +
                    'style="margin-right: 2px;">' +
                    '<i class="icon-edit icon-white"></i>' +
                    '</a>' +
                    '<a class="btn" ' +
                    'href="javascript:void(0);" ' +
                    'onclick="javascript:listRunFlows(\'' + resPageData[i].id + '\');" ' +
                    'style="margin-right: 2px;">' +
                    '<i class="icon-play icon-white"></i>' +
                    '</a>' +
                    '<a class="btn" ' +
                    'href="javascript:void(0);"' +
                    'onclick="javascript:deleteFlowGroup(\'' + resPageData[i].id + '\',\'' + resPageData[i].name + '\');" ' +
                    'style="margin-right: 2px;">' +
                    '<i class="icon-trash icon-white"></i>' +
                    '</a>' +
                    '<a class="btn" ' +
                    'href="javascript:void(0);"' +
                    'onclick="javascript:listSaveFlowGroupTemplate(\'' + resPageData[i].id + '\',\'' + resPageData[i].name + '\');" ' +
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

function openFlowGroup(flowGroupId) {
    var windowOpen = window.open('/piflow-web/mxGraph/drawingBoard?drawingBoardType=GROUP&load=' + flowGroupId + '');
    if (windowOpen == null || typeof(windowOpen)=='undefined'){
        alert('The window cannot be opened. Please check your browser settings.')
    }
}

function update(id, updateName, updateDescription) {
    $("#buttonFlowGroup").attr("onclick", "");
    $("#buttonFlowGroup").attr("onclick", "updateFlowGroup()");
    $("#flowGroupId").val(id);
    $("#flowGroupName").val(updateName);
    $("#description").val(updateDescription);
    layer.open({
        type: 1,
        title: '<span style="color: #269252;">update flow group</span>',
        shadeClose: true,
        closeBtn: false,
        shift: 7,
        closeBtn: 1,
        area: ['580px', '520px'], //Width height
        skin: 'layui-layer-rim', //Add borders
        content: $("#SubmitPage")
    });
}

function saveOrUpdateFlowGroup() {
    var id = $("#flowGroupId").val();
    var flowGroupName = $("#flowGroupName").val();
    var description = $("#description").val();
    if (checkGroupInput(flowGroupName)){
        $.ajax({
            cache: true,//Keep cached data
            type: "get",//Request type post
            url: "/piflow-web/flowGroup/saveOrUpdateFlowGroup",//This is the name of the file where I receive data in the background.
            data: {
                id: id,
                name: flowGroupName,
                description: description
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
                    layer.msg('success ', {icon: 1, shade: 0, time: 2000}, function () {
                        var windowOpen = window.open("/piflow-web/mxGraph/drawingBoard?drawingBoardType=GROUP&load=" + dataMap.flowGroupId);
                        if (windowOpen == null || typeof(windowOpen)=='undefined'){
                            alert('The window cannot be opened. Please check your browser settings.')
                        }
                    });
                } else {
                    layer.msg('failed', {icon: 2, shade: 0, time: 2000}, function () {
                    });
                }
            }
        });
    }
}

function updateFlowGroup() {
    var id = $("#flowGroupId").val();
    var flowGroupName = $("#flowGroupName").val();
    var description = $("#description").val();
    if (checkGroupInput(flowGroupName)){
        $.ajax({
            cache: true,//Keep cached data
            type: "get",//Request type post
            url: "/piflow-web/flowGroup/saveOrUpdateFlowGroup",//This is the name of the file where I receive data in the background.
            data: {
                id: id,
                name: flowGroupName,
                description: description
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
                    layer.msg('success ', {icon: 1, shade: 0, time: 2000}, function () {
                        location.reload();
                    });
                } else {
                    layer.msg('failed', {icon: 2, shade: 0, time: 2000}, function () {
                    });
                }
            }
        });
    }
}

//run
function listRunFlows(loadId, runMode) {
    $('#fullScreen').show();
    var data = {flowGroupId: loadId}
    if (runMode) {
        data.runMode = runMode;
    }
    $.ajax({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/piflow-web/flowGroup/runFlowGroup",//This is the name of the file where I receive data in the background.
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
                    var windowOpen = window.open("/piflow-web/processGroup/getProcessGroupById?parentAccessPath=grapheditor&processGroupId=" + dataMap.processGroupId);
                    //var tempwindow = window.open('_blank');
                    if (windowOpen == null || typeof(windowOpen)=='undefined'){
                        alert('The window cannot be opened. Please check your browser settings.')
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

function deleteFlowGroup(id, name) {
    layer.confirm("Are you sure to delete '" + name + "' ?", {
        btn: ['confirm', 'cancel'] //button
        , title: 'Confirmation prompt'
    }, function () {
        $.ajax({
            cache: true,//Keep cached data
            type: "get",//Request type post
            url: "/piflow-web/flowGroup/deleteFlowGroup",//This is the name of the file where I receive data in the background.
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

function checkGroupInput(flowName) {
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
    return true;
}

function listSaveFlowGroupTemplate(id, name) {
    layer.prompt({
        title: 'please enter the template name',
        formType: 0,
        btn: ['submit', 'cancel']
    }, function (text, index) {
        layer.close(index);
        $.ajax({
            cache: true,//Keep cached data
            type: "get",//Request type post
            url: "/piflow-web/flowGroupTemplate/saveFlowGroupTemplate",//This is the name of the file where I receive data in the background.
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

