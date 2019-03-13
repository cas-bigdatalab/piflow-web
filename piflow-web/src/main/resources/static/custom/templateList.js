var templateTable;

function deleteTemPlate(id, name) {
    layer.confirm("Are you sure to delete '" + name + "' ?", {
        btn: ['confirm', 'cancel'] //按钮
        , title: 'Confirmation prompt'
    }, function () {
        $.ajax({
            cache: true,//保留缓存数据
            type: "get",//为post请求
            url: "/piflow-web/template/deleteTemplate",//这是我在后台接受数据的文件名
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

function initAll(url) {
    window.location.href = url;
}

function uploadTemplate() {
    document.getElementById("templateFile").click();
}

function importFile() {
    if (!FileTypeCheck()) {
        return false;
    }
    var formData = new FormData($('#uploadForm')[0]);
    $.ajax({
        type: 'post',
        url: "/piflow-web/template/upload",
        data: formData,
        cache: false,
        processData: false,
        contentType: false,
    }).success(function (data) {
        var dataMap = JSON.parse(data);
        if (0 !== dataMap.code) {
            layer.msg(dataMap.errMsg, {icon: 1, shade: 0, time: 2000}, function () {
                location.reload();
            });
        } else {
            layer.msg(dataMap.errMsg, {icon: 2, shade: 0, time: 2000}, function () {
            });
        }
    }).error(function () {
        layer.msg("Upload failure", {icon: 2, shade: 0, time: 2000}, function () {
        });
    });
}

function FileTypeCheck() {
    var obj = document.getElementById('templateFile');
    if (obj.value == null || obj.value == '') {
        layer.msg('please upload the XML file', {icon: 2, shade: 0, time: 2000}, function () {
        });
        this.focus()
        return false;
    }
    var length = obj.value.length;
    var charindex = obj.value.lastIndexOf(".");
    var ExtentName = obj.value.substring(charindex, charindex + 4);
    if (!(ExtentName == ".xml")) {
        layer.msg('please upload the XML file', {icon: 2, shade: 0, time: 2000}, function () {
        });
        this.focus()
        return false;
    }
    return true;
}

function downloadTemplate(id) {
    window.location.href = "/piflow-web/template/templateDownload?templateId=" + id;
}

function initDatatableTemplatePage(testTableId, url) {
    templateTable = $('#' + testTableId).DataTable({
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
            "dataSrc": responseHandlerTemplate
        },
        "columns": [
            {"mDataProp": "flowName"},
            {"mDataProp": "templateName"},
            {"mDataProp": "createTime"},
            {"mDataProp": "actions"}
        ]

    });
}

//后台返回的结果
function responseHandlerTemplate(res) {
    let resPageData = res.pageData;
    var pageData = []
    if (resPageData && resPageData.length > 0) {
        for (var i = 0; i < resPageData.length; i++) {
            var data1 = {
                "flowName": "no flowName",
                "templateName": "",
                "createTime": "",
                "actions": ""
            }
            if (resPageData[i]) {
                var actionsHtmlStr = '<div style="width: 100%; text-align: center" >' +
                    '<a class="btn" ' +
                    'href="javascript:void(0);" ' +
                    'style="background-color: #C0C0C0;border: 1px solid;color: #6b5555;" ' +
                    'onclick="javascript:downloadTemplate(\'' + resPageData[i].id + '\');" ' +
                    'title="download template">' +
                    '<i class="icon-download icon-white"></i>' +
                    '</a>' +
                    '<a class="btn" href="javascript:void(0);" ' +
                    'style="background-color: #C0C0C0;border: 1px solid;color: #6b5555;" ' +
                    'onclick="javascript:deleteTemPlate(\'' + resPageData[i].id + '\',\'' + resPageData[i].name + '\'"); ' +
                    'title="delete template" > ' +
                    '<i class="icon-trash icon-white"></i>' +
                    '</a>' +
                    '</div>';
                if (resPageData[i].flow && resPageData[i].flow.name) {
                    data1.flowName = resPageData[i].flow.name;
                }
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

function searchTemplatePage() {
    templateTable.ajax.reload();
}
