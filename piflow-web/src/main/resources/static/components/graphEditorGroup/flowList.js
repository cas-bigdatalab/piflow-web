var flowTable;

function initDatatableFlowPage(testTableId, url) {
    flowTable = $('#' + testTableId).DataTable({
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
                var descriptionHtmlStr = '<div '
                    + 'style="width: 85px;overflow: hidden;text-overflow:ellipsis;white-space:nowrap;" '
                    + 'data-toggle="tooltip" '
                    + 'data-placement="top" '
                    + 'title="' + resPageData[i].description + '">'
                    + resPageData[i].description
                    + '</div>';
                var actionsHtmlStr = '<div style="width: 100%; text-align: center" >'
                    + '<input type="button" class="btn-block" onclick="importFlow(\'' + resPageData[i].id + '\')" value="Import Flow"/>'
                    + '</div>';
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

function importFlow(flowId) {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/piflow-web/flowGroup/copyFlowToGroup",
        data: {"flowId": flowId, "flowGroupId": loadId},
        async: true,
        error: function (request) {
            //alert("Jquery Ajax request error!!!");
            return;
        },
        success: function (data) {
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                alert("cheng_gong cheng_gong cheng_gong cheng_gong cheng_gong cheng_gong cheng_gong cheng_gong");
            } else {
                alert(dataMap.errorMsg);
            }
        }
    });
}