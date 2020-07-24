// Extends EditorUi to update I/O action states based on availability of backend
var graphGlobal = null;
var thisEditor = null;
var sign = true;
var fullScreen = $('#fullScreen');
var pathsCells = [];
var flag = 0;
var timerPath;
var currentStopPageId;
var drawingBoardType = $("#drawingBoardType").val();
var statusgroup, flowPageIdcha, flowGroupdata, cellprecess, flowdatas, removegroupPaths, flowsPagesId;
var index = true;


function add(addParamData, flowId, nodeType) {

    if (flowId && addParamData && addParamData.length > 0 && divValue) {
        var table = document.createElement("table");
        table.style.borderCollapse = "separate";
        table.style.borderSpacing = "0px 5px";
        table.style.marginLeft = "12px";
        table.style.width = "97%";
        var tbody = document.createElement("tbody");
        for (var i = 0; i < addParamData.length; i++) {
            var addData_i = addParamData[i];
            var displayName = document.createElement('input');
            displayName.setAttribute('data-toggle', 'true');
            displayName.setAttribute('class', 'form-control');
            displayName.setAttribute('id', '' + addData_i.id + '');
            displayName.setAttribute('name', '' + addData_i.name + '');
            displayName.setAttribute('onclick', 'attributeTabTd(this,false,"' + nodeType + '")');
            displayName.setAttribute('readonly', 'readonly');
            displayName.style.cursor = "pointer";
            displayName.style.background = "rgb(245, 245, 245)";
            var customValue = (addData_i.value == 'null' ? '' : addData_i.value);
            displayName.setAttribute('value', '' + customValue + '');
            var spanDisplayName = document.createElement('span');
            var spanFlag = document.createElement('span');
            spanFlag.setAttribute('style', 'color:red');
            mxUtils.write(spanDisplayName, '' + addData_i.name + '' + ": ");
            mxUtils.write(spanFlag, '*');
            var img = document.createElement("img");
            img.setAttribute('src', '/piflow-web/img/descIcon.png');
            img.style.cursor = "pointer";
            img.setAttribute('title', '' + addData_i.description + '');
            var tr_1 = document.createElement("tr");
            tr_1.setAttribute('class', 'trTableStop');
            var td1_1 = document.createElement("td");
            var td1_2 = document.createElement("td");
            var td1_3 = document.createElement("td");
            var td1_4 = document.createElement("td");
            td1_1.style.width = "60px";
            td1_2.style.width = "25px";
            //开始appendchild()追加各个元素
            td1_1.appendChild(spanDisplayName);
            td1_2.appendChild(img);
            td1_3.appendChild(displayName);
            td1_4.appendChild(spanFlag);
            tr_1.appendChild(td1_1);
            tr_1.appendChild(td1_2);
            tr_1.appendChild(td1_3);
            tr_1.appendChild(td1_4);
            tbody.appendChild(tr_1);
        }
        table.appendChild(tbody);
        divValue.appendChild(table);
        var flowIdInput = document.createElement("input");
        flowIdInput.setAttribute("id", "updateFlowId")
        flowIdInput.setAttribute("value", flowId);
        flowIdInput.setAttribute("style", "display:none;");
        divValue.appendChild(flowIdInput);
    } else {
        while (divValue && divValue.hasChildNodes()) {
            divValue.removeChild(divValue.firstChild);
        }
    }
}

function cancelFlow() {
    $("#input_node_flow_id").val("");
    $("#input_node_pageId").val("");
    graphGlobal.removeCells(removegroupPaths);
    layer.closeAll()
}

function cancelGroup() {
    graphGlobal.removeCells(removegroupPaths);
    layer.closeAll()
}

//弹框------------------start---------------------------
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
};

//弹框------------------end---------------------------

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

//open xml file
function openXml() {
    ajaxRequest({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/flow/loadData",
        //data:$('#loginForm').serialize(),//Serialize the form
        async: true,//Synchronous Asynchronous
        error: function (request) {//Operation after request failure
            return;
        },
        success: function (data) {//After the request is successful
            loadXml(data);
            console.log("success");
        }
    });
}

//run
function runFlow(runMode) {
    fullScreen.show();
    // console.info("ss");
    var data = {flowId: loadId}
    if (runMode) {
        data.runMode = runMode;
    }
    ajaxRequest({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/flow/runFlow",
        //data:$('#loginForm').serialize(),//Serialize the form
        data: data,
        async: true,//Synchronous Asynchronous
        error: function (request) {//Operation after request failure
            //alert("Request Failed");
            layer.msg("Request Failed", {icon: 2, shade: 0, time: 2000}, function () {
                fullScreen.hide();
            });

            return;
        },
        success: function (data) {//After the request is successful
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                layer.msg(dataMap.errorMsg, {icon: 1, shade: 0, time: 2000}, function () {
                    //Jump to the monitor page after starting successfully
                    var tempWindow = window.open('');
                    if (tempWindow == null || typeof (tempWindow) == 'undefined') {
                        alert('The window cannot be opened. Please check your browser settings.')
                    } else {
                        tempWindow.location = "/mxGraph/drawingBoard?drawingBoardType=PROCESS&processType=PROCESS&load=" + dataMap.processId;
                    }
                });
            } else {
                //alert("Startup failure：" + dataMap.errorMsg);
                layer.msg("Startup failure：" + dataMap.errorMsg, {icon: 2, shade: 0, time: 2000}, function () {
                });
            }
            fullScreen.hide();
        }
    });
}

//run
function runFlowGroup(runMode) {
    fullScreen.show();
    var data = {flowGroupId: loadId}
    if (runMode) {
        data.runMode = runMode;
    }
    ajaxRequest({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/flowGroup/runFlowGroup",
        //data:$('#loginForm').serialize(),//Serialize the form
        data: data,
        async: true,//Synchronous Asynchronous
        error: function (request) {//Operation after request failure
            //alert("Request Failed");
            layer.msg("Request Failed", {icon: 2, shade: 0, time: 2000}, function () {
                fullScreen.hide();
            });

            return;
        },
        success: function (data) {//After the request is successful
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                layer.msg(dataMap.errorMsg, {icon: 1, shade: 0, time: 2000}, function () {
                    //Jump to the monitoring page after starting successfully
                    var windowOpen = window.open("/piflow-web/mxGraph/drawingBoard?drawingBoardType=PROCESS&processType=PROCESS_GROUP&load=" + dataMap.processGroupId, '_blank');
                    //var tempwindow = window.open('_blank');
                    if (windowOpen == null || typeof (windowOpen) == 'undefined') {
                        alert('The window cannot be opened. Please check your browser settings.');
                    }
                });
            } else {
                //alert("Startup failure：" + dataMap.errorMsg);
                layer.msg("Startup failure：" + dataMap.errorMsg, {icon: 2, shade: 0, time: 2000}, function () {
                });
            }
            fullScreen.hide();
        }
    });
}

// check choose port data
function checkChoosePort() {
    var sourcePortVal = '';
    var targetPortVal = '';
    var sourceTypeDivR_R = $('#sourceTypeDivR_R');
    var targetTypeDivR_R = $("#targetTypeDivR_R");
    if (!sourceTypeDivR_R && !targetTypeDivR_R) {
        layer.msg("Page error, please check!", {icon: 2, shade: 0, time: 2000}, function () {
        });
        return false;
    }
    var isSourceRoute = false;
    var isTargetRoute = false;
    var sourceTitleCheckboxR_R = $('#sourceTitleCheckboxR_R');
    var targetTitleCheckboxR_R = $("#targetTitleCheckboxR_R");
    if (sourceTitleCheckboxR_R) {
        var sourceDivType = sourceTypeDivR_R.html();
        if ('Default' === sourceDivType) {
            //'default' type is not verified
        } else if ("Route" === sourceDivType) {
            isSourceRoute = true;
            //'Route' type is not verified
        } else {
            var sourceEffCheckbox = [];
            sourceTitleCheckboxR_R.find("input[type='checkbox']:checked").each(function () {
                if ($(this).prop("disabled") == false) {
                    sourceEffCheckbox[sourceEffCheckbox.length] = $(this);
                }
            });
            if (sourceEffCheckbox.length > 1) {
                layer.msg("'sourcePort'can only choose one", {icon: 2, shade: 0, time: 2000}, function () {
                });
                return false;
            }
            if (sourceEffCheckbox < 1) {
                layer.msg("Please select'sourcePort'", {icon: 2, shade: 0, time: 2000}, function () {
                });
                return false;
            }
            for (var i = 0; i < sourceEffCheckbox.length; i++) {
                var sourcecheckBoxEff = sourceEffCheckbox[i];
                if ('' === sourcePortVal) {
                    sourcePortVal = sourcecheckBoxEff.val();
                } else {
                    sourcePortVal = sourcePortVal + "," + sourcecheckBoxEff.val();
                }
            }
        }
    } else {
        layer.msg("Page error, please check!", {icon: 2, shade: 0, time: 2000}, function () {
        });
        return false;
    }
    if (targetTitleCheckboxR_R) {
        var targetDivType = targetTypeDivR_R.html();
        if ('Default' === targetDivType) {
            //Default type not checked
        } else if ("Route" === targetDivType) {
            isTargetRoute = true;
            //Route type not checked
        } else {
            var targetEffCheckbox = [];
            targetTitleCheckboxR_R.find("input[type='checkbox']:checked").each(function () {
                if ($(this).prop("disabled") == false) {
                    targetEffCheckbox[targetEffCheckbox.length] = $(this);
                }
            });
            if (targetEffCheckbox.length > 1) {
                layer.msg("'targetPort'can only choose one", {icon: 2, shade: 0, time: 2000}, function () {
                });
                return false;
            }
            if (targetEffCheckbox.length < 1) {
                layer.msg("Please select'targetPort'", {icon: 2, shade: 0, time: 2000}, function () {
                });
                return false;
            }
            for (var i = 0; i < targetEffCheckbox.length; i++) {
                var targetcheckBoxEff = targetEffCheckbox[i];
                if ('' === targetPortVal) {
                    targetPortVal = targetcheckBoxEff.val();
                } else {
                    targetPortVal = targetPortVal + "," + targetcheckBoxEff.val();
                }
            }
        }
    } else {
        layer.msg("Page error, please check!", {icon: 2, shade: 0, time: 2000}, function () {
        });
        return false;
    }

    var sourceMxCellId = '';
    var targetMxCellId = '';
    var pathLine = pathsCells[0];
    var pathLineId = pathLine.id;
    var sourceMxCell = pathLine.source;
    var targetMxCell = pathLine.target;
    if (targetMxCell) {
        sourceMxCellId = sourceMxCell.id;
    }
    if (targetMxCell) {
        targetMxCellId = targetMxCell.id;
    }
    var sourceRouteFilterSelectValue = $('#sourceRouteFilterSelectR_R').val();
    var targetRouteFilterSelectValue = $('#targetRouteFilterSelectR_R').val();
    var reqData = {
        "flowId": loadId,
        "pathLineId": pathLineId,
        "sourcePortVal": sourcePortVal,
        "targetPortVal": targetPortVal,
        "sourceId": sourceMxCellId,
        "targetId": targetMxCellId,
        "sourceFilter": sourceRouteFilterSelectValue,
        "targetFilter": targetRouteFilterSelectValue,
        "sourceRoute": isSourceRoute,
        "targetRoute": isTargetRoute
    }
    return reqData;
}

function choosePortNew() {
    if (pathsCells.length > 1) {
        graphGlobal.removeCells(pathsCells);
        layer.msg("Page error, please check!", {icon: 2, shade: 0, time: 2000}, function () {
            layer.closeAll();
            layer.closeAll();
        });
        return false;
    } else {
        var reqData = checkChoosePort();
        if (reqData) {
            ajaxRequest({
                cache: true,
                type: "get",
                url: "/path/savePathsPort",
                data: reqData,
                async: true,
                traditional: true,
                error: function (request) {
                    console.log("error");
                    return;
                },
                success: function (data) {
                    var dataMap = JSON.parse(data);
                    //alert(dataMap);
                    if (200 === dataMap.code) {
                        layer.closeAll();
                    } else {
                        //alert("Port Selection Save Failed");
                        layer.msg("Port Selection Save Failed", {icon: 2, shade: 0, time: 2000}, function () {
                            layer.closeAll();
                        });
                        graphGlobal.removeCells(pathsCells);
                    }
                }
            });
        }
    }
}

function cancelPortAndPathNew() {
    layer.closeAll();
    graphGlobal.removeCells(pathsCells);
}

function crtAnyPort(crtProtInputId, isSource) {
    var crtProtInput = $('#' + crtProtInputId);
    var portNameVal = crtProtInput.val();
    if (portNameVal && '' !== portNameVal) {
        if (!document.getElementById(portNameVal)) {
            var obj = '<div style="display: block;" class="addCheckbox" id="jCheckbox">'
                + '<input type="checkbox" class="addCheckbox" id="' + portNameVal + '" name="' + portNameVal + '" value="' + portNameVal + '">'
                + '<span class="' + portNameVal + '">' + portNameVal + '</span>'
                + '</div>';
            if (isSource) {
                $('#sourceTitleCheckboxR_R').append(obj);
            } else {
                $('#targetTitleCheckboxR_R').append(obj);
            }
            $('.' + portNameVal).text(portNameVal);
        } else {
            layer.msg("Port name occupied!!", {icon: 2, shade: 0, time: 2000}, function () {
            });
        }
    } else {
        //alert("The port name cannot be empty");
        layer.msg("Port name cannot be empty", {icon: 2, shade: 0, time: 2000}, function () {
        });
    }
}

function saveCheckpoints(stopId) {
    //alert("ssss");
    var isCheckpoint = 0;
    if ($('#isCheckpoint').is(':checked')) {
        isCheckpoint = 1;
    }
    ajaxRequest({
        cache: true,
        type: "POST",
        url: "/stops/updateStopsById",
        data: {
            stopId: stopId,
            isCheckpoint: isCheckpoint
        },
        async: true,
        traditional: true,
        error: function (request) {
            layer.msg("Failure to mark'Checkpoint'", {icon: 2, shade: 0, time: 2000}, function () {
            });
            return;
        },
        success: function (data) {
            var dataMap = JSON.parse(data);
            //alert(dataMap);
            //console.log("attribute update success");
            if (200 === dataMap.code) {
                layer.msg("Successful modification of the tag'Checkpoint'", {
                    icon: 1,
                    shade: 0,
                    time: 2000
                }, function () {
                });
            } else {
                layer.msg("Failed to modify the tag'Checkpoint'", {icon: 2, shade: 0, time: 2000}, function () {
                });
            }

        }
    });
}

function saveTemplateFun(url) {
    var templateType = drawingBoardType;
    var getXml = thisEditor.getGraphXml();
    var xml_outer_html = getXml.outerHTML;
    layer.prompt({
        title: 'please enter the template name',
        formType: 0,
        btn: ['submit', 'cancel']
    }, function (text, index) {
        layer.close(index);
        ajaxRequest({
            cache: true,//Keep cached data
            type: "POST",//Request type post
            url: "/flowTemplate/saveFlowTemplate",
            data: {
                value: xml_outer_html,
                load: loadId,
                name: text,
                templateType: templateType
            },
            async: true,
            error: function (request) {//Operation after request failure
                console.log(" save template error");
                return;
            },
            success: function (data) {//After the request is successful
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

function uploadTemplate() {
    document.getElementById("uploadFile").click();
}

function uploadFlowGroupTemplateBtn() {
    document.getElementById("flowTemplateFile").click();
}

function uploadTemplateFile(element) {
    if (!FileTypeCheck(element)) {
        return false;
    }
    if (url) {
        return false;
    }
    var formData = new FormData($('#uploadForm')[0]);
    ajaxRequest({
        type: 'post',
        url: '/flowTemplate/uploadXmlFile',
        data: formData,
        cache: false,
        processData: false,
        contentType: false,
    }).success(function (data) {
        var dataMap = JSON.parse(data);
        if (200 === dataMap.code) {
            layer.msg(dataMap.errorMsg, {icon: 1, shade: 0, time: 2000}, function () {
            });
        } else {
            layer.msg(dataMap.errorMsg, {icon: 2, shade: 0, time: 2000}, function () {
            });
        }
    }).error(function () {
        layer.msg("Upload failure", {icon: 2, shade: 0, time: 2000}, function () {
        });
    });
}

function FileTypeCheck(element) {
    if (element.value == null || element.value == '') {
        layer.msg('please upload the XML file', {icon: 2, shade: 0, time: 2000}, function () {
        });
        this.focus()
        return false;
    }
    var length = element.value.length;
    var charindex = element.value.lastIndexOf(".");
    var ExtentName = element.value.substring(charindex, charindex + 4);
    if (!(ExtentName == ".xml")) {
        layer.msg('please upload the XML file', {icon: 2, shade: 0, time: 2000}, function () {
        });
        this.focus()
        return false;
    }
    return true;
}

function loadingXml(id, loadId) {
    var loadType = Format.customizeType;
    fullScreen.show();
    ajaxRequest({
        type: 'post',
        data: {
            templateId: id,
            loadType: loadType,
            load: loadId
        },
        async: true,
        url: "/flowTemplate/loadingXmlPage",
    }).success(function (data) {
        var dataMap = JSON.parse(data);
        var icon_code = 2;
        if (200 === dataMap.code) {
            icon_code = 1;
        }
        fullScreen.hide();
        layer.msg(dataMap.errorMsg, {icon: icon_code, shade: 0.7, time: 2000}, function () {
            window.location.reload();
        });
    }).error(function () {
        fullScreen.hide();
    });
}

function openTemplateList() {
    if (isExample) {
        layer.msg('This is an example, you can\'t edit', {icon: 2, shade: 0, time: 2000}, function () {
        });
        return;
    }
    var url = "";
    var functionNameStr = "";
    if ('TASK' !== Format.customizeType && 'GROUP' !== Format.customizeType) {
        return;
    }
    ajaxRequest({
        url: "/flowTemplate/flowTemplateList",
        type: "post",
        async: false,
        success: function (data) {
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                var temPlateList = dataMap.temPlateList;
                var showSelectDivHtml = '<div style="width: 100%;height: 146px;position: relative;">';
                var showOptionHtml = '';
                for (var i = 0; i < temPlateList.length; i++) {
                    showOptionHtml += ("<option value=" + temPlateList[i].id + " >" + temPlateList[i].name + "</option>");
                }
                var showSelectHtml = 'There is no template, please create';
                var loadTemplateBtn = '';
                if (showOptionHtml) {
                    showSelectHtml = ('<div style="width: 100%;text-align: center;">'
                        + '<select name="loadingXmlSelect" id="loadingXmlSelectNew" style="width: 80%;margin-top: 15px;">'
                        + '<option value=\'-1\' >------------please choose------------</option>'
                        + showOptionHtml
                        + '</select>'
                        + '</div>');
                    loadTemplateBtn = '<div style="position: absolute;bottom: 12px;right: 10px;">'
                        + '<input type="button" class="btn" value="Submit" onclick="loadTemplateFun()"/>'
                        + '</div>';
                }
                showSelectDivHtml += (showSelectHtml + loadTemplateBtn + '</div>');
                layer.open({
                    type: 1,
                    title: '<span style="color: #269252;">Please choose</span>',
                    shadeClose: false,
                    resize: false,
                    closeBtn: 1,
                    shift: 7,
                    area: ['500px', '200px'], //Width height
                    skin: 'layui-layer-rim', //Add borders
                    content: showSelectDivHtml
                });
            } else {
                layer.msg("No template, please create", {time: 2000});
            }
        }
    });
}

function loadTemplateFun() {
    var id = $("#loadingXmlSelectNew").val();
    if (id == '-1') {
        layer.msg('Please choose template', {icon: 2, shade: 0, time: 2000}, function () {
        });
        return;
    }

    var name = $("#loadingXmlSelect option:selected").text();
    layer.open({
        title: 'LoadTemplate',
        content: 'Are you sure you want to load ' + name + '？',
        btn: ['submit', 'cancel'],
        yes: function (index, layero) {
            loadingXml(id, loadId);
            var oDiv = document.getElementById("divloadingXml");
            oDiv.style.display = "none";
        },
        btn2: function (index, layero) {
            var oDiv = document.getElementById("divloadingXml");
            oDiv.style.display = "none";
        }, cancel: function () {
            var oDiv = document.getElementById("divloadingXml");
            oDiv.style.display = "none";
        }
    });
}

function getRunningProcessList() {
    ajaxRequest({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/process/getRunningProcessListData",
        data: {"flowId": loadId},
        async: true,
        error: function (request) {//Operation after request failure
            return;
        },
        success: function (data) {//
            $('#runningProcessID').remove();
            $('#rightSidebarID').append(data);
        }
    });
}

function layuiOpenWindowDivFunc(title, contentHtml) {
    layer.open({
        type: 1,
        title: '<span style="color: #269252;">' + title + '</span>',
        shadeClose: false,
        closeBtn: 0,
        shift: 7,
        area: ['580px', '520px'], //Width height
        skin: 'layui-layer-rim', //Add borders
        content: contentHtml
    });
}

function closeChoosePortWindow() {
    layer.closeAll();
    graphGlobal.removeCells(pathsCells);
}

//***************************************************************************************************************
//***************************************************************************************************************
//***************************************************************************************************************
//***************************************************************************************************************
//***************************************************************************************************************
//***************************************************************************************************************
//***************************************************************************************************************

var consumedFlag;

var flowGroupData = [
    {
        groupName: "Group",
        dataList: [
            {
                name: "group",
                img_name: "group",
                img_type: ".png",
                description: 'group component'
            }
        ]
    }, {
        groupName: "Task",
        dataList: [
            {
                name: "flow",
                img_name: "flow",
                img_type: ".png",
                description: 'Task component'
            }
        ]
    },
    {
        groupName: "Text",
        dataList: [
            {
                name: "text",
                img_name: "",
                img_type: "",
                description: 'Text'
            }
        ]
    }
];

function initFlowGroupDrawingBoardData(loadId, parentAccessPath) {
    $('#fullScreen').show();
    ajaxRequest({
        type: "get",
        url: "/flowGroup/drawingBoardData",
        async: false,
        data: {
            load: loadId,
            parentAccessPath: parentAccessPath
        },
        success: function (data) {//Operation after request successful
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                parentsId = dataMap.parentsId;
                xmlDate = dataMap.xmlDate;
                maxStopPageId = dataMap.maxStopPageId;
                isExample = dataMap.isExample;
                if (dataMap.mxGraphComponentList) {
                    Sidebar.prototype.component_data = dataMap.mxGraphComponentList
                }
            } else {
                window.location.href = (web_header_prefix + "/error/404");
            }
            $('#fullScreen').hide();
        },
        error: function (request) {//Operation after request failure
            window.location.href = (web_header_prefix + "/error/404");
            return;
        }
    });
}

function initGraph() {
    Format.prototype.isShowTextCell = true;
    Menus.prototype.customRightMenu = ['runAll'];
    Menus.prototype.customCellRightMenu = ['run'];
    Actions.prototype.RunAll = runFlowGroup;
    EditorUi.prototype.saveGraphData = saveXml;
    Format.hideSidebar(true, true);
    Format.customizeType = "GROUP";
    var editorUiInit = EditorUi.prototype.init;
    $("#right-group-wrap")[0].style.display = "block";

    EditorUi.prototype.init = function () {
        editorUiInit.apply(this, arguments);
        graphGlobal = this.editor.graph;
        thisEditor = this.editor;
        this.actions.get('export').setEnabled(false);
        //Monitoring event
        graphGlobal.addListener(mxEvent.CELLS_ADDED, function (sender, evt) {
            console.log(evt);
            if (isExample) {
                prohibitEditing(evt, 'ADD');
            } else {
                addMxCellOperation(evt);
            }
        });
        graphGlobal.addListener(mxEvent.CELLS_MOVED, function (sender, evt) {
            console.log(evt);
            if (isExample) {
                prohibitEditing(evt, 'MOVED');
            } else {
                movedMxCellOperation(evt);
            }
        });
        graphGlobal.addListener(mxEvent.CELLS_REMOVED, function (sender, evt) {
            console.log(evt);
            if (isExample) {
                prohibitEditing(evt, 'REMOVED');
            } else {
                removeMxCellOperation(evt);
            }
        });

        graphGlobal.addListener(mxEvent.CLICK, function (sender, evt) {
            consumedFlag = evt.consumed ? true : false;
            mxEventClickFunc(evt.properties.cell, consumedFlag);
        });
        graphGlobal.addListener(mxEvent.DOUBLE_CLICK, function (sender, evt) {
            openProcessMonitor(evt);
            if (evt.properties.cell.style && (evt.properties.cell.style).indexOf("text\;") === 0) {
                if (graphGlobal.isEnabled()) {
                    graphGlobal.startEditingAtCell();
                }
            }
        });
        if (xmlDate) {
            var xml = mxUtils.parseXml(xmlDate);
            var node = xml.documentElement;
            var dec = new mxCodec(node.ownerDocument);
            dec.decode(node, graphGlobal.getModel());
            eraseRecord()
        }
    };

    // Adds required resources (disables loading of fallback properties, this can only
    // be used if we know that all keys are defined in the language specific file)
    mxResources.loadDefaultBundle = false;
    var bundle = mxResources.getDefaultBundle(RESOURCE_BASE, mxLanguage) ||
        mxResources.getSpecialBundle(RESOURCE_BASE, mxLanguage);

    // Fixes possible asynchronous requests
    mxUtils.getAll([bundle, STYLE_PATH + '/default.xml'], function (xhr) {
        // Adds bundle text to resources
        mxResources.parse(xhr[0].getText());

        // Configures the default graph theme
        var themes = new Object();
        themes[Graph.prototype.defaultThemeName] = xhr[1].getDocumentElement();

        // Main
        new EditorUi(new Editor(urlParams['chrome'] == '0', themes));
    }, function () {
        document.body.innerHTML = '<center style="margin-top:10%;">Error loading resource files. Please check browser console.</center>';
    });
    EditorUi.prototype.menubarHeight = 48;
    EditorUi.prototype.menubarShow = false;
    EditorUi.prototype.customToobar = true;
    ClickSlider();
}

//mxGraph click event
function mxEventClickFunc(cell, consumedFlag) {
    $("#flowGroup_info_inc_id").hide();
    $("#flowGroup_path_inc_id").hide();
    $("#flowGroup_property_inc_id").hide();
    if (index) {
        $(".right-group").toggleClass("open-right");
        $(".ExpandSidebar").toggleClass("ExpandSidebar-open");
        index = false
    }
    if (!consumedFlag) {
        $("#flowGroup_info_inc_id").show();
        // info
        queryFlowGroupInfo(loadId);
        return;
    }
    var cells_arr = graphGlobal.getSelectionCells();
    if (cells_arr.length !== 1) {
        $("#flowGroup_info_inc_id").show();
        // info
        queryFlowGroupInfo(loadId);
    } else {
        var selectedCell = cells_arr[0]
        if (selectedCell && (selectedCell.edge === 1 || selectedCell.edge)) {
            $("#flowGroup_path_inc_id").show();
            queryPathInfo(selectedCell.id, loadId)
        } else if (selectedCell && selectedCell.style && (selectedCell.style).indexOf("image\;") === 0) {
            $("#flowGroup_property_inc_id").show();
            queryFlowOrFlowGroupProperty(selectedCell.id, loadId);
        } else if (selectedCell && selectedCell.style && (selectedCell.style).indexOf("text\;") === 0) {
            $("#flowGroup_info_inc_id").show();
            // info
            queryFlowGroupInfo(loadId);
        }
    }
}

//Erase drawing board records
function eraseRecord() {
    thisEditor.lastSnapshot = new Date().getTime();
    thisEditor.undoManager.clear();
    thisEditor.ignoredChanges = 0;
    thisEditor.setModified(false);
}

function selectCellByPageId(pageId, isClick) {
    if (!pageId) {
        return;
    }
    var s_cell = graphGlobal.getModel().getCell(pageId);
    graphGlobal.addSelectionCell(s_cell);
    if (isClick) {
        mxEventClickFunc(s_cell, true);
    }
}

//load xml file
function loadXml(loadStr) {
    var xml = mxUtils.parseXml(loadStr);
    var node = xml.documentElement;
    var dec = new mxCodec(node.ownerDocument);
    dec.decode(node, graphGlobal.getModel());
    eraseRecord();
}

// add node
function addMxCellOperation(evt) {
    var cells = evt.properties.cells;
    statusgroup = cells[0].value;

    var cellArrayRemove = [];
    var cellArrayAdd = [];

    cells.forEach(cellFor => {
        if (cellFor && cellFor.edge) {
            var cellForSource = cellFor.source;
            var cellForTarget = cellFor.target;

            if (!cellForSource || !cellForTarget) {
                cellArrayRemove.push(cellFor);
                return;
            }
            if (!cellForSource.style || !cellForTarget.style) {
                cellArrayRemove.push(cellFor);
                return;
            }
            if ((cellForSource.style).indexOf("text\;") === 0 || (cellForTarget.style).indexOf("text\;") === 0) {
                cellArrayRemove.push(cellFor);
                return;
            }
            cellArrayAdd.push(cellFor);
        } else {
            cellArrayAdd.push(cellFor);
        }
    });
    var mxCellArrayAdd = [];
    cellArrayAdd.forEach(cellFor => {
        var mxCellAdd = graphCellToMxCellVo(cellFor);
        if (mxCellAdd) {
            mxCellArrayAdd.push(mxCellAdd);
        }
    });
    graphGlobal.removeCells(cellArrayRemove);
    if (cells.length != cellArrayRemove.length) {
        var time, time1;
        ajaxRequest({
            cache: true,//Keep cached data
            type: "POST",//Request type post
            url: "/mxGraph/addMxCellAndData",
            data: JSON.stringify({
                mxCellVoList: mxCellArrayAdd,
                loadId: loadId
            }),
            contentType: 'application/json;charset=utf-8',
            async: true,//Synchronous Asynchronous
            error: function (request) {//Operation after request failure
                layer.msg('Add failed, refresh page after 1 second', {icon: 2, shade: 0, time: 2000}, function () {
                    graphGlobal.removeCells(cellArrayAdd);
                    //window.location.reload();
                });
                return;
            },
            success: function (data) {//After the request is successful
                var dataMap = JSON.parse(data);
                console.log(dataMap);
                if (200 === dataMap.code) {
                    var addNodeIdAndPageIdList = dataMap.addNodeIdAndPageIdList;
                    var flagA = true;
                    if (addNodeIdAndPageIdList && addNodeIdAndPageIdList.length === 1) {
                        var addNodeIdAndPageId = addNodeIdAndPageIdList[0];
                        if ("flow" === addNodeIdAndPageId.type) {
                            openNewFlowWindow(addNodeIdAndPageId.id, addNodeIdAndPageId.pageId);
                        } else if ("flowGroup" === addNodeIdAndPageId.type) {
                            openNewFlowGroupWindow(addNodeIdAndPageId.id, addNodeIdAndPageId.pageId);
                        }
                    }
                    if (flagA && graphGlobal.isEnabled()) {
                        graphGlobal.startEditingAtCell();
                    }
                    thisEditor.setModified(false);
                    console.log("Add save success");
                } else {
                    layer.msg("Add save fail", {icon: 2, shade: 0, time: 2000}, function () {
                    });
                    console.log("Add save fail");
                    fullScreen.hide();
                }
            }

        });
    }
    if ('cellsAdded' == evt.name) {
        consumedFlag = evt.consumed ? true : false;
        mxEventClickFunc(evt.properties.cell, consumedFlag);
    }
}

// del node
function removeMxCellOperation(evt) {
    saveXml(null, 'REMOVED');
}

// moved node
function movedMxCellOperation(evt) {
    statusgroup = ""
    if (evt.properties.disconnect) {
        saveXml(null, 'MOVED');   // preservation method
    }
}

// example operation
function prohibitEditing(evt, operationType) {
    if ('ADD' === operationType || 'REMOVED' === operationType) {
        layer.msg("This is an example, you can't add, edit or delete", {
            icon: 2,
            shade: 0,
            time: 2000
        }, function () {

        });
    } else if ('MOVED' === operationType) {
        consumedFlag = evt.consumed ? true : false;
        mxEventClickFunc(evt, consumedFlag);
    }
    ajaxRequest({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/mxGraph/eraseRecord",
        data: {},
        async: true,
        error: function (request) {//Operation after request failure
            if ('ADD' === operationType || 'REMOVED' === operationType) {
                location.reload();
            }
            eraseRecord();
            return;
        },
        success: function (data) {//After the request is successful
            if ('ADD' === operationType || 'REMOVED' === operationType) {
                location.reload();
            }
            eraseRecord();
        }
    });
}

// cell to mxCellVo
function graphCellToMxCellVo(cellObject) {
    if (cellObject) {
        var mxCellVoObject = {};
        mxCellVoObject.pageId = cellObject.id;
        mxCellVoObject.parent = cellObject.parent.id;
        mxCellVoObject.style = cellObject.style;
        mxCellVoObject.value = cellObject.value;
        mxCellVoObject.vertex = cellObject.vertex;
        mxCellVoObject.edge = cellObject.edge;
        if (cellObject.source) {
            mxCellVoObject.source = cellObject.source.id;
        }
        if (cellObject.target) {
            mxCellVoObject.target = cellObject.target.id;
        }
        mxCellVoObject.mxGeometryVo = {};
        if (cellObject.geometry) {
            mxCellVoObject.mxGeometryVo.as = "geometry";
            mxCellVoObject.mxGeometryVo.x = cellObject.geometry.x;
            mxCellVoObject.mxGeometryVo.y = cellObject.geometry.y;
            mxCellVoObject.mxGeometryVo.width = cellObject.geometry.width;
            mxCellVoObject.mxGeometryVo.height = cellObject.geometry.height;
            mxCellVoObject.mxGeometryVo.relative = cellObject.geometry.relative;
        }
        return mxCellVoObject;
    }
    return;
}

//Double click event
function openProcessMonitor(evt) {
    var cellFor = evt.properties.cell;
    if (cellFor.style && (cellFor.style).indexOf("text\;") === 0) {
    } else {
        ajaxRequest({
            cache: true,
            type: "POST",
            url: "/flowGroup/findFlowByGroup",
            data: {"pageId": cellFor.id, "fId": loadId},
            async: true,
            error: function (request) {
                //alert("Jquery Ajax request error!!!");
                return;
            },
            success: function (data) {
                var dataMap = JSON.parse(data);
                if (200 === dataMap.code) {
                    if ('flow' === dataMap.nodeType) {
                        var flow_obj = dataMap.flowVo;
                        window.location.href = "/piflow-web/mxGraph/drawingBoard?drawingBoardType=TASK&parentAccessPath=flowGroupList&load=" + flow_obj.id;
                    } else if ('flowGroup' === dataMap.nodeType) {
                        var flowGroup_obj = dataMap.flowGroupVo;
                        window.location.href = "/piflow-web/mxGraph/drawingBoard?drawingBoardType=GROUP&parentAccessPath=flowGroupList&load=" + flowGroup_obj.id;
                    }
                } else {
                    console.log(dataMap.errorMsg);
                }
            }
        });
    }
}

//Save XML file and related information
function saveXml(paths, operType) {
    var getXml = thisEditor.getGraphXml();
    var xml_outer_html = getXml.outerHTML;
    var time, time1
    ajaxRequest({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/mxGraph/saveDataForGroup",
        //data:$('#loginForm').serialize(),//Serialize the form
        data: {
            imageXML: xml_outer_html,
            load: loadId,
            operType: operType
        },
        async: true,//Synchronous Asynchronous
        error: function (request) {//Operation after request failure
            return;
        },
        success: function (data) {//After the request is successful
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                console.log(operType + " save success");
                if (statusgroup == "group" && operType == "ADD") {
                    $("#buttonGroup").attr("onclick", "");
                    $("#buttonGroup").attr("onclick", "saveOrUpdateFlowGroup()");
                    $("#flowGroupId").val("");
                    $("#flowGroupName").val("");
                    $("#flowGroup_description").val("");
                    layer.open({
                        type: 1,
                        title: '<span style="color: #269252;">create flow group</span>',
                        shadeClose: false,
                        shade: 0.3,
                        closeBtn: 1,
                        shift: 7,
                        area: ['580px', '520px'], //Width height
                        skin: 'layui-layer-rim', //Add borders
                        content: $("#flowGroup_SubmitPage"),
                        success: function () {
                            $(".layui-layer-page").css("z-index", "1998910151");

                            queryFlowOrFlowGroupProperty(flowsPagesId)

                            setTimeout(() => {
                                if (flowGroupdata == undefined) {
                                    var index2 = 0
                                    clearInterval(time)
                                    clearInterval(time1)
                                    time1 = setInterval(() => {
                                        if (index2 < 4) {
                                            queryFlowOrFlowGroupProperty(flowsPagesId)
                                            index2++
                                        } else if (index2 >= 4) {
                                            // layer.msg("Network Anomaly", {icon: 5})
                                            alert("Network Anomaly")
                                            clearInterval(time1)
                                            index2 = 0
                                        } else {
                                            clearInterval(time)
                                            clearInterval(time1)
                                        }
                                    }, 300)
                                }
                            }, 500)

                        },
                        cancel: function (index, layero) {
                            graphGlobal.removeCells(removegroupPaths);
                            layer.close(index)

                            return false;
                        }
                    });
                } else if (statusgroup == "flow" && operType == "ADD") {
                    $("#buttonFlowCancel").attr("onclick", "");
                    $("#buttonFlowCancel").attr("onclick", "cancelFlow()");
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
                        shadeClose: false,
                        shade: 0.3,
                        closeBtn: 1,
                        shift: 7,
                        area: ['580px', '520px'], //Width height
                        skin: 'layui-layer-rim', //Add borders
                        content: $("#flow_SubmitPage"),
                        success: function () {
                            queryFlowOrFlowGroupProperty(flowsPagesId)


                            setTimeout(() => {
                                if (flowdatas == undefined) {
                                    var index2 = 0
                                    clearInterval(time)
                                    clearInterval(time1)
                                    time1 = setInterval(() => {
                                        if (index2 < 4) {
                                            queryFlowOrFlowGroupProperty(flowsPagesId)
                                            index2++
                                        } else if (index2 >= 4) {
                                            // layer.msg("Network Anomaly", {icon: 5})
                                            alert("Network Anomaly")
                                            clearInterval(time1)
                                            index2 = 0
                                        } else {
                                            clearInterval(time)
                                            clearInterval(time1)
                                        }
                                    }, 300)
                                }
                            }, 500)
                        },

                        cancel: function (index, layero) {
                            graphGlobal.removeCells(removegroupPaths);
                            getRunningProcessList()
                            layer.close(index)
                            return false;
                        }
                    });
                } else {

                    if (graphGlobal.isEnabled()) {
                        graphGlobal.startEditingAtCell();
                    }
                }
                thisEditor.setModified(false);

            } else {
                //alert(operType + " save fail");
                layer.msg(operType + " save fail", {icon: 2, shade: 0, time: 2000}, function () {
                });
                console.log(operType + " save fail");
                fullScreen.hide();
            }

        }

    });
}

function openNewFlowWindow(id, pageId) {
    $("#input_node_flow_id").val(id);
    $("#input_node_flow_pageId").val(pageId);
    $("#flowName").val("");
    $("#description").val("");
    $("#driverMemory").val('1g');
    $("#executorNumber").val('1');
    $("#executorMemory").val('1g');
    $("#executorCores").val('1');
    layer.open({
        type: 1,
        title: '<span style="color: #269252;">Create Flow</span>',
        shadeClose: false,
        shade: 0.3,
        closeBtn: 0,
        shift: 7,
        area: ['580px', '520px'], //Width height
        skin: 'layui-layer-rim', //Add borders
        content: $("#flow_SubmitPage")
    });
}

function openNewFlowGroupWindow(id, pageId) {
    $("#input_node_flowGroup_id").val(id);
    $("#input_node_flowGroup_pageId").val(pageId);
    $("#flowGroupName").val("");
    $("#flowGroup_description").val("");
    layer.open({
        type: 1,
        title: '<span style="color: #269252;">create flow group</span>',
        shadeClose: false,
        shade: 0.3,
        closeBtn: 0,
        shift: 7,
        area: ['580px', '520px'], //Width height
        skin: 'layui-layer-rim', //Add borders
        content: $("#flowGroup_SubmitPage")
    });
}

function queryFlowGroupInfo(loadId) {
    $("#flowGroup_info_inc_loading").show();
    $("#flowGroup_info_inc_load_fail").hide();
    $("#flowGroup_info_inc_no_data").hide();
    $("#flowGroup_info_inc_load_data").hide();
    ajaxRequest({
        data: {"load": loadId},
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/flowGroup/queryFlowGroupData",
        async: true,//Synchronous Asynchronous
        error: function (request) {//Operation after request failure
            $("#flowGroup_info_inc_loading").hide();
            $("#flowGroup_info_inc_load_fail").show();
            return;
        },
        success: function (data) {//After the request is successful
            $("#flowGroup_info_inc_loading").hide();
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                var flowGroupVo = dataMap.flowGroupVo;
                if (flowGroupVo) {
                    $("#span_flowGroupVo_id").text(flowGroupVo.id ? flowGroupVo.id : "No content");
                    $("#span_flowGroupVo_name").text(flowGroupVo.name ? flowGroupVo.name : "No content");
                    $("#span_flowGroupVo_description").text(flowGroupVo.description ? flowGroupVo.description : "No content");
                    $("#span_flowGroupVo_crtDttmStr").text(flowGroupVo.crtDttmString ? flowGroupVo.crtDttmString : "No content");
                    $("#span_flowGroupVo_flowsCounts").text(flowGroupVo.flowVoList ? flowGroupVo.flowVoList.length : "0");
                    $("#flowGroup_info_inc_load_data").show();
                } else {
                    $("#flowGroup_info_inc_no_data").show();
                }
            } else {
                $("#flowGroup_info_inc_load_fail").show();
            }
        }
    });
}

function queryPathInfo(id, loadId) {
    ajaxRequest({
        cache: true,
        type: "POST",
        url: "/flowGroupPath/queryPathInfoFlowGroup",
        data: {"id": id, "fid": loadId},
        async: true,
        error: function (request) {
            //alert("Jquery Ajax request error!!!");
            return;
        },
        success: function (data) {
            var dataMap = JSON.parse(data);
            console.log(dataMap);
            if (200 === dataMap.code) {
                var queryInfo = dataMap.queryInfo;
                if ("" != queryInfo) {
                    $("#AttributeInfoId").hide();
                    $("#containerID").show();
                    $("#basicInfoId").html('path info');
                    $('#basicInfoId').css('text-align', '');
                    $('#basicInfoId').css('background-color', '');
                    $('#basicInfoId').css('border-style', '');
                    $('#basicInfoId').css('height', '27px');
                    $("#customizeBasic_td_1_1_span_id").html(param_values.customizeBasic_td_1_1_span_children);
                    $("#customizeBasic_td_2_1_span_id").html(param_values.customizeBasic_td_2_1_span_children);
                    $("#customizeBasic_td_3_1_span_id").html(param_values.customizeBasic_td_3_1_span_children);
                    $("#customizeBasic_td_4_1_span_id").html(param_values.customizeBasic_td_4_1_span_children);
                    $("#customizeBasic_td_5_1_span_id").html(param_values.customizeBasic_td_5_1_span_children);
                    $("#customizeBasic_td_6_1_span_id").html(param_values.customizeBasic_td_6_1_span_children);
                    $("#customizeBasic_td_7_1_span_id").html(param_values.customizeBasic_td_7_1_span_children);
                    $("#customizeBasic_td_1_2_button_id").hide();
                    $("#customizeBasic_td_3_2_label_id").html(queryInfo.inport);
                    $("#customizeBasic_td_4_2_label_id").html(queryInfo.outport);
                    $("#customizeBasic_td_7_2_label_id").html(queryInfo.crtDttmString);
                    $("#customizeBasic_td_1_2_span_id").html(queryInfo.pageId);
                    $("#customizeBasic_td_2_2_span_id").html(queryInfo.flowGroupVo.name);
                    $("#customizeBasic_td_5_2_label_id").html(queryInfo.flowFrom);
                    $("#customizeBasic_td_6_2_label_id").html(queryInfo.flowTo);
                }
            } else {
                console.log("Path attribute query null");
            }
        }
    });
}

function queryFlowOrFlowGroupProperty(pageId, loadId) {
    $('#cell_flowGroup_property_inc_loading').show();
    $('#cell_flowGroup_property_inc_load_fail').hide();
    $('#cell_flowGroup_property_inc_no_data').hide();
    $('#cell_flowGroup_property_inc_load_data').hide();
    ajaxRequest({
        cache: true,
        type: "POST",
        url: "/flowGroup/queryIdInfo",
        data: {"fId": loadId, "pageId": pageId},
        async: true,
        error: function (request) {
            $('#cell_flowGroup_property_inc_loading').hide();
            $('#cell_flowGroup_property_inc_load_fail').show();
            return;
        },
        success: function (data) {
            $('#cell_flowGroup_property_inc_loading').hide();
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                var nodeType = dataMap.nodeType;
                if ("flowGroup" === nodeType) {
                    var flowGroupVo = dataMap.flowGroupVo;
                    $("#div_cell_flowVo_basicInfo_id").hide();
                    $("#div_cell_flowVo_attributeInfo_id").hide();
                    $("#div_cell_flowGroupVo_basicInfo_id").show();
                    $("#div_cell_flowGroupVo_attributeInfo_id").show();

                    // ----------------------- cell flowGroup baseInfo start -----------------------
                    $("#tr_cell_flowGroupVo_name_info").show();
                    $("#tr_cell_flowGroupVo_name_input").hide()
                    $('#input_cell_flowGroupVo_id').val(flowGroupVo.id);
                    $('#input_cell_flowGroupVo_pageId').val(flowGroupVo.pageId);
                    $('#span_cell_flowGroupVo_name').text(flowGroupVo.name);
                    $('#span_cell_flowGroupVo_description').text(flowGroupVo.description);
                    $('#span_cell_flowGroupVo_crtDttmString').text(flowGroupVo.crtDttmString);
                    $('#span_cell_flowGroupVo_flowQuantity').text(flowGroupVo.flowQuantity);
                    $('#span_cell_flowGroupVo_flowGroupQuantity').text(flowGroupVo.flowGroupQuantity);

                    if (isExample) {
                        $('#btn_show_update_group').hide();
                    }
                    // ----------------------- cell flowGroup baseInfo end   -----------------------

                    // ----------------------- cell flowGroup AttributeInfo start -----------------------
                    $('#input_cell_flowGroupVo_description').val(flowGroupVo.description);
                    $('#input_cell_flowGroupVo_description').attr("name", "description");
                    $('#input_cell_flowGroupVo_description').attr("onclick", "openUpdateCellsProperty(this ,'flowGroup')");
                    // ----------------------- cell flowGroup AttributeInfo end -----------------------

                    $('#cell_flowGroup_property_inc_load_data').show();
                } else if ("flow" === nodeType) {
                    var flowVo = dataMap.flowVo;
                    $("#div_cell_flowGroupVo_basicInfo_id").hide();
                    $("#div_cell_flowGroupVo_attributeInfo_id").hide();
                    $("#div_cell_flowVo_basicInfo_id").show();
                    $("#div_cell_flowVo_attributeInfo_id").show();

                    // ----------------------- cell flow baseInfo start -----------------------
                    $("#tr_cell_flowVo_name_info").show();
                    $("#tr_cell_flowVo_name_input").hide();
                    $('#input_cell_flowVo_id').val(flowVo.id);
                    $('#input_cell_flowVo_pageId').val(flowVo.pageId);
                    $('#span_cell_flowVo_name').text(flowVo.name);
                    $('#span_cell_flowVo_description').text(flowVo.description);
                    $('#span_cell_flowVo_driverMemory').text(flowVo.driverMemory);
                    $('#span_cell_flowVo_executorCores').text(flowVo.executorCores);
                    $('#span_cell_flowVo_executorMemory').text(flowVo.executorMemory);
                    $('#span_cell_flowVo_executorNumber').text(flowVo.executorNumber);
                    $('#span_cell_flowVo_crtDttmString').text(flowVo.crtDttmString);
                    $('#span_cell_flowVo_stopQuantity').text(flowVo.stopQuantity);
                    if (isExample) {
                        $('#btn_show_update').hide();
                    }
                    // ----------------------- cell flow baseInfo end   -----------------------

                    // ----------------------- cell flow AttributeInfo start -----------------------
                    $('#input_cell_flowVo_description').val(flowVo.description);
                    $('#input_cell_flowVo_description').attr("name", "description");
                    $('#input_cell_flowVo_description').attr("onclick", "openUpdateCellsProperty(this ,'flow')");
                    $('#input_cell_flowVo_driverMemory').val(flowVo.driverMemory);
                    $('#input_cell_flowVo_driverMemory').attr("name", "driverMemory");
                    $('#input_cell_flowVo_driverMemory').attr("onclick", "openUpdateCellsProperty(this ,'flow')");
                    $('#input_cell_flowVo_executorCores').val(flowVo.executorCores);
                    $('#input_cell_flowVo_executorCores').attr("name", "executorCores");
                    $('#input_cell_flowVo_executorCores').attr("onclick", "openUpdateCellsProperty(this ,'flow')");
                    $('#input_cell_flowVo_executorMemory').val(flowVo.executorMemory);
                    $('#input_cell_flowVo_executorMemory').attr("name", "executorMemory");
                    $('#input_cell_flowVo_executorMemory').attr("onclick", "openUpdateCellsProperty(this ,'flow')");
                    $('#input_cell_flowVo_executorNumber').val(flowVo.executorNumber);
                    $('#input_cell_flowVo_executorNumber').attr("name", "executorNumber");
                    $('#input_cell_flowVo_executorNumber').attr("onclick", "openUpdateCellsProperty(this ,'flow')");
                    // ----------------------- cell flow AttributeInfo end   -----------------------
                    $('#cell_flowGroup_property_inc_load_data').show();
                } else {
                    $('#cell_flowGroup_property_inc_no_data').show();
                }
            } else {
                $('#cell_flowGroup_property_inc_load_fail').show();
            }
        }
    });
}

function getFlowList() {
    var window_width = $(window).width();//Get browser window width
    var window_height = $(window).height();//Get browser window height
    ajaxRequest({
        type: "GET",//Request type post
        url: "/page/flow/getFlowListImport",//This is the name of the file where I receive data in the background.
        error: function (request) {//Operation after request failure
            return;
        },
        success: function (data) {//Operation after request successful
            layer.open({
                type: 1,
                title: '<span style="color: #269252;">Flows</span>',
                shade: 0,
                shadeClose: false,
                closeBtn: 1,
                shift: 7,
                area: [(window_width / 2) + 'px', (window_height - 100) + 'px'], //Width height
                skin: 'layui-layer-rim', //Add borders
                content: data
            });
        }
    });
}

function ClickSlider() {
    $(".triggerSlider").click(function () {
        var flag = ($(".triggerSlider i:first").hasClass("fa fa-angle-right fa-2x"));
        if (flag === false)
            $(".triggerSlider i").removeClass("fa fa-angle-left fa-2x ").toggleClass("fa fa-angle-right fa-2x");
        else
            $(".triggerSlider i").removeClass("fa fa-angle-right fa-2x").toggleClass("fa fa-angle-left fa-2x");

        $(".right-group").toggleClass("open-right");
        $(".ExpandSidebar").toggleClass("ExpandSidebar-open");
        $(this).toggleClass("triggerSlider-open");
        index = !index
    });
}

function updateFlowGroupCellsNameById(selectNodesType) {
    if ('flow' === selectNodesType && $("#input_cell_flowVo_name").val() === $("#span_cell_flowVo_name").text()) {
        isShowUpdateCellsName(false, selectNodesType);
        return;
    }
    if ('flowGroup' === selectNodesType && $("#input_cell_flowGroupVo_name").val() === $("#span_cell_flowGroupVo_name").text()) {
        isShowUpdateCellsName(false, selectNodesType);
        return;
    }
    var requestDataParam = {
        parentId: loadId,
        updateType: selectNodesType
    };
    if ('flow' === selectNodesType) {
        requestDataParam.currentNodeId = $("#input_cell_flowVo_id").val();
        requestDataParam.currentNodePageId = $("#input_cell_flowVo_pageId").val();
        requestDataParam.name = $("#input_cell_flowVo_name").val();
    } else if ('flowGroup' === selectNodesType) {
        requestDataParam.currentNodeId = $("#input_cell_flowGroupVo_id").val();
        requestDataParam.currentNodePageId = $("#input_cell_flowGroupVo_pageId").val();
        requestDataParam.name = $("#input_cell_flowGroupVo_name").val();
    }
    ajaxRequest({
        cache: true,
        type: "POST",
        url: "/flowGroup/updateFlowNameById",
        data: requestDataParam,
        async: true,
        traditional: true,
        error: function (request) {
            console.log("attribute update error");
            return;
        },
        success: function (data) {
            var dataMap = JSON.parse(data);
            console.log(dataMap);
            if (200 === dataMap.code) {
                //reload xml
                loadXml(dataMap.XmlData);

                selectCellByPageId(requestDataParam.currentNodePageId, false);
                layer.msg("attribute update success", {icon: 1, shade: 0, time: 2000}, function () {
                });
                if ('flow' === selectNodesType) {
                    $("#span_cell_flowVo_name").text(dataMap.nameContent);
                } else if ('flowGroup' === selectNodesType) {
                    $("#span_cell_flowGroupVo_name").text(dataMap.nameContent);
                }
                isShowUpdateCellsName(false, selectNodesType);
            } else {
                layer.msg(dataMap.errorMsg, {icon: 2, shade: 0, time: 2000}, function () {
                });
            }
        }
    });
}

//update stops name button
function isShowUpdateCellsName(flag, selectNodesType) {
    if (selectNodesType && 'flow' === selectNodesType) {
        if (flag) {
            $("#input_cell_flowVo_name").val($("#span_cell_flowVo_name").text());
            $("#tr_cell_flowVo_name_info").hide();
            $("#tr_cell_flowVo_name_input").show();
        } else {
            $("#tr_cell_flowVo_name_info").show();
            $("#tr_cell_flowVo_name_input").hide();
        }
    } else if (selectNodesType && 'flowGroup' === selectNodesType) {
        if (flag) {
            $("#input_cell_flowGroupVo_name").val($("#span_cell_flowGroupVo_name").text());
            $("#tr_cell_flowGroupVo_name_info").hide();
            $("#tr_cell_flowGroupVo_name_input").show();
        } else {
            $("#tr_cell_flowGroupVo_name_info").show();
            $("#tr_cell_flowGroupVo_name_input").hide();
        }
    }
}

//update stops property
function openUpdateCellsProperty(e, selectNodesType) {
    var updateCellsPropertyTemplateClone = $("#updateCellsPropertyTemplate").clone();
    updateCellsPropertyTemplateClone.find("#cellsValue").attr("id", "cellsPropertyValue");
    updateCellsPropertyTemplateClone.find("#buttonCells").attr("id", "cellsPropertyValueBtn");
    var locked = e.getAttribute('locked');
    if (isExample || 'true' == locked) {
        updateCellsPropertyTemplateClone.find("#cellsPropertyValue").attr("disabled", "disabled");
        updateCellsPropertyTemplateClone.find("#cellsPropertyValueBtn").hide();
    }
    updateCellsPropertyTemplateClone.find("#cellsPropertyValue").css("background-color", "");
    updateCellsPropertyTemplateClone.find("#cellsPropertyValue").attr('name', e.name);
    updateCellsPropertyTemplateClone.find("#cellsPropertyValue").text(e.value);

    if ("flowGroup" === selectNodesType) {
        var flowId = $("#input_cell_flowGroupVo_id").val();
        updateCellsPropertyTemplateClone.find("#cellsPropertyValueBtn").attr("onclick", ("updateFlowGroupAttributes('" + flowId + "','" + e.id + "','cellsPropertyValue',this);"));
    } else {
        var flowGroupId = $("#input_cell_flowVo_id").val();
        updateCellsPropertyTemplateClone.find("#cellsPropertyValueBtn").attr("onclick", ("updateFlowAttributes('" + flowGroupId + "','" + e.id + "','cellsPropertyValue',this)"));
    }
    var p = $(e).offset();
    var openWindowCoordinate = [(p.top + 34) + 'px', (document.body.clientWidth - 300) + 'px'];
    console.log(openWindowCoordinate);
    layer.open({
        type: 1,
        title: e.name,
        shadeClose: true,
        closeBtn: 0,
        shift: 7,
        anim: 5,//Pop up from top
        shade: 0.1,
        resize: true,//No stretching
        //move: false,//No dragging
        offset: openWindowCoordinate,//coordinate
        area: ['290px', '204px'], //Width Height
        content: updateCellsPropertyTemplateClone.html()
    });
    $("#cellsValue").focus();
    $("#cellsPropertyValue").focus();
}

function updateFlowAttributes(flowId, propertyId, updateContentId, e) {
    var p = $(e).offset();
    var content = document.getElementById(updateContentId).value;
    if (!content) {
        $("#" + updateContentId + "").css("background-color", "#FFD39B");
        $("#" + updateContentId + "").focus();
        return;
    }
    $('#' + propertyId).val(content);
    ajaxRequest({
        cache: true,
        type: "POST",
        url: "/flow/updateFlowBaseInfo",
        data: {
            "id": flowId,
            "description": $('#input_cell_flowVo_description').val(),
            "driverMemory": $('#input_cell_flowVo_driverMemory').val(),
            "executorCores": $('#input_cell_flowVo_executorCores').val(),
            "executorMemory": $('#input_cell_flowVo_executorMemory').val(),
            "executorNumber": $('#input_cell_flowVo_executorNumber').val()

        },
        async: true,
        traditional: true,
        error: function (request) {
            console.log("attribute update error");
            return;
        },
        success: function (data) {
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                layer.msg('success', {
                    icon: 1,
                    shade: 0,
                    time: 2000,
                    offset: ['' + p.top - 30 + 'px', '' + p.left + 50 + 'px']
                }, function () {
                });
                var flowVo = dataMap.flowVo;
                $('#input_cell_flowVo_description').val(flowVo.description)
                $('#input_cell_flowVo_driverMemory').val(flowVo.driverMemory);
                $('#input_cell_flowVo_executorCores').val(flowVo.executorCores);
                $('#input_cell_flowVo_executorMemory').val(flowVo.executorMemory);
                $('#input_cell_flowVo_executorNumber').val(flowVo.executorNumber);
                //baseInfo
                $('#span_cell_flowVo_description').text(flowVo.description);
                $('#span_cell_flowVo_driverMemory').text(flowVo.driverMemory);
                $('#span_cell_flowVo_executorCores').text(flowVo.executorCores);
                $('#span_cell_flowVo_executorMemory').text(flowVo.executorMemory);
                $('#span_cell_flowVo_executorNumber').text(flowVo.executorNumber);
            } else {
                layer.msg('', {icon: 2, shade: 0, time: 2000}, function () {
                });
            }
            layer.closeAll('page');
            console.log("attribute update success");
        }
    });
}

function updateFlowGroupAttributes(flowGroupId, propertyId, updateContentId, e) {
    var p = $(e).offset();
    var content = document.getElementById(updateContentId).value;
    if (!content) {
        $("#" + updateContentId + "").css("background-color", "#FFD39B");
        $("#" + updateContentId + "").focus();
        return;
    }
    $('#' + propertyId).val(content);
    ajaxRequest({
        cache: true,
        type: "POST",
        url: "/flowGroup/updateFlowGroupBaseInfo",
        data: {
            "id": flowGroupId,
            "description": $("#input_cell_flowGroupVo_description").val()
        },
        async: true,
        traditional: true,
        error: function (request) {
            console.log("attribute update error");
            return;
        },
        success: function (data) {
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                layer.msg('success', {
                    icon: 1,
                    shade: 0,
                    time: 2000,
                    offset: ['' + p.top - 30 + 'px', '' + p.left + 50 + 'px']
                }, function () {
                });
                var flowGroupVo = dataMap.flowGroupVo;
                $("#input_cell_flowGroupVo_description").val(flowGroupVo.description)
                //baseInfo
                $("#span_cell_flowGroupVo_description").text(flowGroupVo.description);
            } else {
                layer.msg('', {icon: 2, shade: 0, time: 2000}, function () {
                });
            }
            layer.closeAll('page');
            console.log("attribute update success");
        }
    });
}

//flow Information popup-----
function saveFlow() {
    var currentId = $("#input_node_flow_id").val();
    var currentPageId = $("#input_node_flow_pageId").val();
    if (!currentId && !currentPageId) {
        alert("Please click the close button and drag it again to create");
        return
    }
    var flowName = $("#flowName").val();
    var description = $("#description").val();
    var driverMemory = $("#driverMemory").val();
    var executorNumber = $("#executorNumber").val();
    var executorMemory = $("#executorMemory").val();
    var executorCores = $("#executorCores").val();
    if (!checkFlowInput(flowName, description, driverMemory, executorNumber, executorMemory, executorCores)) {
        // layer.closeAll()
        // layer.msg('flowName Can not be empty', {icon: 2, shade: 0, time: 2000});
        alert("flowName Can not be empty")
    } else {
        ajaxRequest({
            cache: true,
            type: "POST",
            url: "/flow/updateFlowBaseInfo",
            data: {
                fId: loadId,
                id: currentId,
                pageId: currentPageId,
                name: flowName,
                driverMemory: driverMemory,
                executorCores: executorCores,
                executorMemory: executorMemory,
                executorNumber: executorNumber,
                description: description
            },
            async: true,
            traditional: true,
            error: function (request) {
                console.log("attribute update error");
                return;
            },
            success: function (data) {
                var dataMap = JSON.parse(data);
                var msgtrue = false
                if (200 === dataMap.code) {
                    $("#input_node_flow_id").val("");
                    $("#input_node_flow_pageId").val("");

                    //reload xml
                    var xml = mxUtils.parseXml(dataMap.XmlData);
                    loadXml(dataMap.XmlData);
                    layer.msg(dataMap.errorMsg, {icon: 1, shade: 0, time: 2000}, function () {
                        selectCellByPageId(currentPageId, true);
                        queryFlowOrFlowGroupProperty(currentPageId, loadId);
                        layer.closeAll();
                    });
                } else {
                    layer.msg(dataMap.errorMsg, {icon: 2, shade: 0, time: 2000}, function () {
                    });
                }
            }
        });
    }
};

//flowGroup Information popup-----
function saveOrUpdateFlowGroup() {
    var currentId = $("#input_node_flowGroup_id").val();
    var currentPageId = $("#input_node_flowGroup_pageId").val();
    if (!currentId && !currentPageId) {
        alert("Please click the close button and drag it again to create")
        return
    }
    var flowGroupName = $("#flowGroupName").val();
    var description = $("#flowGroup_description").val();
    if (!checkGroupInput(flowGroupName)) {
        layer.msg('flowName Can not be empty', {icon: 2, shade: 0, time: 2000});
    } else {
        var requestDataParam = {
            flowGroupId: loadId,
            flowId: flowGroupdata.id,
            pageId: flowGroupdata.pageId,
            updateType: "flowGroup",
            name: flowGroupName
        };
        var msgtrue = false
        ajaxRequest({
            cache: true,
            type: "POST",
            url: Format.customizeTypeAttr.updateNameUrl,
            data: requestDataParam,
            async: true,
            traditional: true,
            error: function (request) {
                console.log("attribute update error");
                return;
            },
            success: function (data) {
                var dataMap = JSON.parse(data);
                if (200 === dataMap.code) {
                    //reload xml
                    var xml = mxUtils.parseXml(dataMap.XmlData);
                    var node = xml.documentElement;
                    var dec = new mxCodec(node.ownerDocument);
                    dec.decode(node, graphGlobal.getModel());
                    $("#customizeBasic_td_1_2_input2_id").val(flowGroupName);
                    layer.closeAll()
                    msgtrue = true
                    layer.msg(dataMap.errorMsg, {icon: 1, shade: 0, time: 2000}, function () {
                        // findBasicInfo(results);
                    });

                } else {
                    // layer.closeAll()
                    layer.msg(dataMap.errorMsg, {icon: 2, shade: 0, time: 2000}, function () {
                    });
                    alert(dataMap.errorMsg)
                    msgtrue = false
                }
                ajaxRequest({
                    cache: true,
                    type: "POST",
                    url: "/flowGroup/updateFlowGroupBaseInfo",
                    data: {
                        id: flowGroupdata.id,
                        description: description,
                        name: flowGroupName
                    },
                    async: true,
                    traditional: true,
                    error: function (request) {
                        console.log("attribute update error");
                        return;
                    },
                    success: function (data) {
                        var dataMap = JSON.parse(data);
                        if (200 === dataMap.code) {
                            var flowGroupVo = dataMap.flowGroupVo;
                            // descriptionObj.val(flowGroupVo.description)
                            //baseInfo
                            $('#customizeBasic_td_2_2_span_id').text(flowGroupVo.description);
                        } else {
                            layer.msg('', {icon: 2, shade: 0, time: 2000}, function () {
                            });
                        }
                        if (msgtrue) {
                            layer.closeAll();
                        }

                        console.log("attribute update success");
                    }
                });

            }
        });


    }

}

