var dataSourceTable;

// Replace the contents of the attribute
function updateSAttributes(updateHtmlStr, findType, attrName, oldContent, newContent) {
    var updateHtmlStr = $(updateHtmlStr);
    var idSelectObj = updateHtmlStr.find(findType);
    if (idSelectObj && idSelectObj.length > 0) {
        for (var i = 0; i < idSelectObj.length; i++) {
            var id_select_obj_i = idSelectObj[i];
            var id_select_obj_i_content = $(id_select_obj_i).attr(attrName);
            if (id_select_obj_i_content) {
                id_select_obj_i_content = id_select_obj_i_content.replace(new RegExp(oldContent, "gm"), newContent);
                $(id_select_obj_i).attr(attrName, id_select_obj_i_content);
            }
        }
    }
    return updateHtmlStr;
}

//Batch replacement
function replaceContent(sourceHtml, oldContent, newContent) {
    var splitArr = oldContent.split("R_R");
    var nameOldContent = splitArr.length === 2 ? splitArr[1] : splitArr[0];
    sourceHtml = updateSAttributes(sourceHtml, "div", "id", oldContent, "R_R" + newContent);
    sourceHtml = updateSAttributes(sourceHtml, "div a", "onclick", oldContent, "R_R" + newContent);
    sourceHtml = updateSAttributes(sourceHtml, "label input", "name", nameOldContent, newContent);
    sourceHtml = updateSAttributes(sourceHtml, "div input", "name", nameOldContent, newContent);
    return sourceHtml;
}

//Add a custom module
function addCustomProperty(copyId, targetId) {
    var sourceHtml = $("#" + copyId).clone();
    var targetObj = $("#" + targetId);
    var number = targetObj.children().length;
    var completedHtml = replaceContent(sourceHtml, "1Copy", number);
    $("#" + targetId).append(completedHtml.html());
}

//Delete custom module
function removeCustomModule(removeId, listId) {
    var listTotal = $("#" + listId).children().length;
    if (listTotal && listTotal > 1) {
        var number = removeId.split("R_R");
        if (number && number.length == 2) {
            $("#" + removeId).parent().remove();
            var forListTotal = listTotal;
            var currentNumber = number[1];
            while (currentNumber < forListTotal) {
                var nestNumber = currentNumber;
                var oldContentStr = "R_R" + (++nestNumber);
                var updateHtml = $("#" + number[0] + oldContentStr).parent();
                replaceContent(updateHtml, oldContentStr, currentNumber);
                currentNumber++;
            }
        } else {
            layer.msg("Incoming Id error please check！！");
        }
    } else {
        layer.msg("Please keep at least one！！");
    }
}

function dataSourceOpen(dataSourceId) {
    $.ajax({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/piflow-web/datasource/getDataSourceInputPage",//This is the name of the file where I receive data in the background.
        //data:$('#loginForm').serialize(),//Serialize the form
        data: {"dataSourceId": dataSourceId},
        async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            layer.msg("Request Failed", {icon: 2, shade: 0, time: 2000}, function () {
            });
            return;
        },
        success: function (data) {//Operation after request successful
            layer.open({
                type: 1,
                title: '<span style="color: #269252;">data source</span>',
                shadeClose: true,
                closeBtn: 1,
                shift: 7,
                area: ['580px', '550px'], //Width height
                skin: 'layui-layer-rim', //Add borders
                content: data
            });
        }
    });

}

function initDataTablePage(testTableId, url) {
    dataSourceTable = $('#' + testTableId).DataTable({
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
            "dataSrc": responseHandler
        },
        "columns": [
            {"mDataProp": "dataSourceName"},
            {"mDataProp": "dataSourceDescription"},
            {"mDataProp": "dataSourceType"},
            {"mDataProp": "dataAction"},
        ]

    });
}

//Results returned in the background
function responseHandler(res) {
    var resPageData = res.pageData;
    var pageData = []
    if (resPageData && resPageData.length > 0) {
        for (var i = 0; i < resPageData.length; i++) {
            var data = {
                "dataSourceName": "<div name='dataSourceName'></div>",
                "dataSourceDescription": "",
                "dataSourceType": "<div name='dataSourceType'>No State</div>",
                "dataAction": "<div name='dataAction'>None</div>"
            }

            if (resPageData[i]) {
                var descriptionHtmlStr = '<div '
                    + 'style="width: 85px;overflow: hidden;text-overflow:ellipsis;white-space:nowrap;" '
                    + 'data-toggle="tooltip" '
                    + 'data-placement="top" '
                    + 'title="' + resPageData[i].dataSourceDescription + '">'
                    + resPageData[i].dataSourceDescription
                    + '</div>';
                var dataAction = '<a class="btn" '
                    + 'href="javascript:void(0);" '
                    + 'onclick="javascript:dataSourceOpen(\'' + resPageData[i].id + '\');" '
                    + 'style="margin-right: 2px;">'
                    + '<i class="icon-edit icon-white"></i>'
                    + '</a>'
                    + '<a class="btn" '
                    + 'href="javascript:void(0);" '
                    + 'onclick="javascript:delDataSource(\'' + resPageData[i].id + '\');" '
                    + 'title="delete template"> '
                    + '<i class="icon-trash icon-white"></i>'
                    + '</a>';
                if (resPageData[i].dataSourceName) {
                    data.dataSourceName = '<div name="dataSourceName" style="word-wrap: break-word;">' + resPageData[i].dataSourceName + '</div>';
                }
                if (descriptionHtmlStr) {
                    data.dataSourceDescription = descriptionHtmlStr;
                }
                if (resPageData[i].dataSourceType) {
                    data.dataSourceType = '<div name="dataSourceType" style="word-wrap: break-word;">' +
                        resPageData[i].dataSourceType + '</div>';
                }
                if (dataAction) {
                    data.dataAction = dataAction;
                }
            }
            pageData.push(data);
        }
    }
    return pageData;
}

function search1() {
    dataSourceTable.ajax.reload();
}

function changeDataSourceType(select) {
    $('#typeId').html("");
    $('#template_type_id').val("");
    $('#template_type_div_id').hide();
    if (select.value) {
        $('#typeContentId').show();
        if ('other' === select.value) {
            $('#customOtherDatasource').show();
            var datasourcePropertyHtml = $('#customKeyValueCopy').clone().html();
            var completedHtml = replaceContent(datasourcePropertyHtml, "1Copy", 0);
            $('#custom_property_list').html(completedHtml);
            $('#template_type_id').val("other");
        } else {
            $('#customOtherDatasource').hide();
            loadDataSourceById(select.value);
        }
    } else {
        $('#typeContentId').hide();
    }
}

function loadDataSourceById(dataSourceId) {
    $.ajax({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/piflow-web/datasource/getDatasourceById",//This is the name of the file where I receive data in the background.
        //data:$('#loginForm').serialize(),//Serialize the form
        data: {"id": dataSourceId},
        async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            layer.msg("Request Failed", {icon: 2, shade: 0, time: 2000}, function () {
            });
            return;
        },
        success: function (data) {//Operation after request successful
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                var template = dataMap.data;
                var dataSourcePropertyVoList = template.dataSourcePropertyVoList;
                var datasourcePropertyHtml = '';
                if (dataSourcePropertyVoList && dataSourcePropertyVoList.length > 0) {
                    for (var j = 0; j < dataSourcePropertyVoList.length; j++) {
                        var dataSourcePropertyVo = dataSourcePropertyVoList[j];
                        datasourcePropertyHtml += ('<div class="layui-form-item layui-form-text">'
                            + '<label class="layui-form-label">' + dataSourcePropertyVo.name + '</label>'
                            + '<input style="display:none;" '
                            + 'name="dataSourcePropertyVoList[' + j + '].name" '
                            + 'value="' + dataSourcePropertyVo.name + '" />'
                            + '<div class="layui-input-block">'
                            + '<input class="layui-input" autocomplete="off"'
                            + 'name="dataSourcePropertyVoList[' + j + '].value" '
                            + 'placeholder="please input name..." style="width: 95%;"/>'
                            + '</div>'
                            + '</div>');

                    }
                }
                $('#template_type_id').val(template.dataSourceType);
                $('#typeId').html(datasourcePropertyHtml);
            }

        }
    });
}

function saveOrUpdateDataSource(data) {
    $.ajax({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/piflow-web/datasource/saveOrUpdate",//This is the name of the file where I receive data in the background.
        data: data,
        async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            layer.msg("Request Failed", {icon: 2, shade: 0, time: 2000}, function () {
            });
            return;
        },
        success: function (data) {//Operation after request successful
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                layer.msg(dataMap.errorMsg, {icon: 1, shade: 0, time: 1000}, function () {
                    window.location.reload();
                    console.log(data);
                });
            }
        }
    });
}

function delDataSource(datasourceId) {
    $.ajax({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/piflow-web/datasource/deleteDataSource",//This is the name of the file where I receive data in the background.
        //data:$('#loginForm').serialize(),//Serialize the form
        data: {"dataSourceId": datasourceId},
        async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            layer.msg("Request Failed", {icon: 2, shade: 0, time: 2000}, function () {
            });
            return;
        },
        success: function (data) {//Operation after request successful
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                layer.msg(dataMap.errorMsg, {icon: 1, shade: 0, time: 2000}, function () {
                    window.location.reload();
                });
            }

        }
    });
}
