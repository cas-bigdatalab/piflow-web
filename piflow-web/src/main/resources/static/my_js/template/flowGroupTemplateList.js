var flowGroupTemplateTable;

function initDatatableFlowGroupTemplatePage(testTableId, url) {
    flowGroupTemplateTable = $('#' + testTableId).DataTable({
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
            "dataSrc": responseHandlerTemplate
        },
        "columns": [
            {"mDataProp": "templateName"},
            {"mDataProp": "createTime"},
            {"mDataProp": "actions"}
        ]

    });
}

//Results returned in the background
function responseHandlerTemplate(res) {
    var resPageData = res.pageData;
    var pageData = []
    if (resPageData && resPageData.length > 0) {
        for (var i = 0; i < resPageData.length; i++) {
            var data1 = {
                "templateName": "",
                "createTime": "",
                "actions": ""
            }
            if (resPageData[i]) {
                var actionsHtmlStr = '<div style="width: 100%; text-align: center" >' +
                    '<a class="btn" ' +
                    'href="javascript:void(0);" ' +
                    'onclick="javascript:downloadFlowGroupTemplate(\'' + resPageData[i].id + '\');" ' +
                    'title="download template">' +
                    '<i class="icon-download icon-white"></i>' +
                    '</a>' +
                    '<a class="btn" href="javascript:void(0);" ' +
                    'onclick="javascript:deleteFlowGroupTemPlate(\'' + resPageData[i].id + '\',\'' + resPageData[i].name + '\'); "' +
                    'title="delete template" > ' +
                    '<i class="icon-trash icon-white"></i>' +
                    '</a>' +
                    '</div>';
                if (resPageData[i].name) {
                    data1.templateName = resPageData[i].name;
                }
                if (resPageData[i].crtDttm) {
                    data1.createTime = resPageData[i].crtDttm;
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


function initLayerTableFlowGroupTemplatePage(testTableId, url, searchInputId) {
    var table = "";
    layui.use('table', function () {
        table = layui.table;

        //Method-level rendering
        table.render({
            elem: '#' + testTableId
            , url: url
            , cols: [[
                {field: 'name', title: 'TemplateName', sort: true},
                {field: 'crtDttm', title: 'CreateTime', sort: true},
                {
                    field: 'right', title: 'Actions', sort: true, height: 100, templet: function (data) {
                        return responseHandlerFlowGroupTemplate(data);
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

//Results returned in the background
function responseHandlerFlowGroupTemplate(data) {
    if (!data) {
        return "";
    }
    var downloadHtmlStr = '<a class="btn" ' +
        'href="javascript:void(0);" ' +
        'onclick="javascript:downloadFlowGroupTemplate(\'' + data.id + '\');" ' +
        'title="download template">' +
        '<i class="icon-download icon-white"></i>' +
        '</a>';

    var delHtmlStr = '<a class="btn" href="javascript:void(0);" ' +
        'onclick="javascript:deleteFlowGroupTemPlate(\'' + data.id + '\',\'' + data.name + '\'); "' +
        'title="delete template" > ' +
        '<i class="icon-trash icon-white"></i>' +
        '</a>';

    return '<div style="width: 100%; text-align: center">' + downloadHtmlStr + delHtmlStr + '</div>';
}

function deleteFlowGroupTemPlate(id, name) {
    layer.confirm("Are you sure to delete '" + name + "' ?", {
        btn: ['confirm', 'cancel'] //button
        , title: 'Confirmation prompt'
    }, function () {
        ajaxRequest({
            cache: true,//Keep cached data
            type: "get",//Request type post
            url: "/flowTemplate/deleteFlowTemplate",//This is the name of the file where I receive data in the background.
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
                    layer.msg('Delete failed', {icon: 2, shade: 0, time: 2000});
                }
            }
        });
    }, function () {
    });
}

function initAll(url) {
    window.location.href = url;
}

function listUploadFlowGroupTemplateBtn() {
    document.getElementById("flowTemplateFile").click();
}

function listUploadFlowGroupTemplate() {
    if (!listFileTypeCheck()) {
        return false;
    }
    var formData = new FormData($('#uploadForm')[0]);
    ajaxRequest({
        type: 'post',
        url: "/flowTemplate/uploadXmlFile",
        data: formData,
        cache: false,
        processData: false,
        contentType: false,
    }).success(function (data) {
        var dataMap = JSON.parse(data);
        if (200 === dataMap.code) {
            layer.msg(dataMap.errorMsg, {icon: 1, shade: 0, time: 2000}, function () {
                location.reload();
            });
        } else {
            layer.msg(dataMap.errorMsg, {icon: 2, shade: 0, time: 2000});
        }
    }).error(function () {
        layer.msg("Upload failure", {icon: 2, shade: 0, time: 2000});
    });
}

function listFileTypeCheck() {
    var obj = document.getElementById('flowTemplateFile');
    if (obj.value == null || obj.value == '') {
        layer.msg('please upload the XML file', {icon: 2, shade: 0, time: 2000});
        this.focus()
        return false;
    }
    var length = obj.value.length;
    var charindex = obj.value.lastIndexOf(".");
    var ExtentName = obj.value.substring(charindex, charindex + 4);
    if (!(ExtentName == ".xml")) {
        layer.msg('please upload the XML file', {icon: 2, shade: 0, time: 2000});
        this.focus()
        return false;
    }
    return true;
}

function downloadFlowGroupTemplate(id) {
    window.location.href = "/piflow-web/flowTemplate/templateDownload?flowTemplateId=" + id;
}

