// Extends EditorUi to update I/O action states based on availability of backend
var graphGlobal = null;
var thisEditor = null;
var sign = true;
var fullScreen = $('#fullScreen');
var pathsCells = [];
var flag = 0;
var timerPath;
var currentStopPageId;


function initGraph() {
    var editorUiInit = EditorUi.prototype.init;

    EditorUi.prototype.init = function () {
        editorUiInit.apply(this, arguments);
        graphGlobal = this.editor.graph;
        thisEditor = this.editor;

        //Monitoring event
        graphGlobal.addListener(mxEvent.CELLS_ADDED, function (sender, evt) {
            processListener(evt, "ADD");
        });
        graphGlobal.addListener(mxEvent.CELLS_MOVED, function (sender, evt) {
            processListener(evt, "MOVED");

        });
        graphGlobal.addListener(mxEvent.CELLS_REMOVED, function (sender, evt) {
            processListener(evt, "REMOVED");
        });
        graphGlobal.addListener(mxEvent.CLICK, function (sender, evt) {
            findBasicInfo(evt);
        });
        graphGlobal.addListener(mxEvent.DOUBLE_CLICK, function (sender, evt) {
            openDrawingBoard(evt);
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
}

//Double click event
function openDrawingBoard(evt) {
    var cellfor = evt.properties.cell;
    if (cellfor.style && (cellfor.style).indexOf("text\;") === 0) {
    } else {
        $.ajax({
            cache: true,
            type: "POST",
            url: "/piflow-web/flow/findFlowByGroup",
            data: {"flowPageId": cellfor.id, "fId": loadId},
            async: true,
            error: function (request) {
                //alert("Jquery Ajax request error!!!");
                return;
            },
            success: function (data) {
                var dataMap = JSON.parse(data);
                if (200 === dataMap.code) {
                    var flow_obj = dataMap.flow
                    var tempWindow = window.open("/piflow-web/grapheditor/home?load=" + flow_obj.id + "&parentAccessPath=flowGroupList");
                    if (tempWindow == null || typeof (tempWindow) == 'undefined') {
                        alert('The window cannot be opened. Please check your browser settings.')
                    }
                } else {
                    console.log(dataMap.errorMsg);
                }
            }
        });
    }
}

function findBasicInfo(evt) {
    flag = 0;
    var id = null;
    var value = null;
    var ss = [];
    //Set values for different events
    var cells = evt.properties.cells;
    if (null != cells) {
        //cellsAdded and cellsMoved operations go here
        value = cells[0].value;
        id = cells[0].id;
    } else {
        cells = evt.properties;
        if (null != cells.cell) {
            //Click on the Stop operation
            value = cells.cell.value;
            id = cells.cell.id;
        } else {
            //When you add a stop to the artboard for the first time, the drag process does not get the id.
            queryFlowProperty(maxFlowPageId);
        }
    }
    if (typeof (cells) != "undefined" && null != id && "" != id && "null" != id) {
        if (document.getElementById("stopsNameLabel"))
            document.getElementById('stopsNameLabel').value = value;
        //Check the line is the case
        if (cells.cell && cells.cell.edge) {
            if (cells.cell.target && cells.cell.source) {
                //Query path
                queryPathInfo(id);
            }
        } else {
            //Query stops and attribute information;
            queryFlowProperty(id);
        }
    }
}

function queryFlowProperty(flowPageId) {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/piflow-web/flow/queryIdInfo",
        data: {"flowPageId": flowPageId, "fid": loadId},
        async: true,
        error: function (request) {
            //alert("Jquery Ajax request error!!!");
            return;
        },
        success: function (data) {
            if ("" != data) {
                var dataId = (null != data.id ? data.id : "");
                var dataName = (null != data.name ? data.name : "");
                var dataPageId = (null != data.pageId ? data.pageId : "");
                var dataDriverMemory = (null != data.driverMemory ? data.driverMemory : "");
                var dataExecutorCores = (null != data.executorCores ? data.executorCores : "");
                var dataExecutorMemory = (null == data.executorMemory ? "" : data.executorMemory);
                var dataExecutorNumber = (null == data.executorNumber ? "" : data.executorNumber);
                var dataDescription = (null != data.description ? data.description : "");
                var dataCrtDttmString = (null == data.crtDttmString ? "" : data.crtDttmString);
                var stopQuantity = (data.stopsVoList ? data.stopsVoList.length : "0");
                $('#flowNameSpan').text(dataName);
                $('#flowNameLabel').attr("value", dataName);
                $('#flowNameLabel').attr("name", dataId);
                $('#flowValueInput').attr("value", dataName);
                $('#flowValueInput').attr("name", dataPageId);
                $('#flowDescription').text(dataDescription);
                $('#flowDriverMemory').text(dataDriverMemory);
                $('#flowExecutorCores').text(dataExecutorCores);
                $('#flowExecutorMemory').text(dataExecutorMemory);
                $('#flowExecutorNumber').text(dataExecutorNumber);
                $('#flowCreateDate').text(dataCrtDttmString);
                $('#stopQuantity').text(stopQuantity);
                add();
                var addDatas = [
                    {id: "id0", name: "driverMemory", value: dataDriverMemory, description: "driverMemory"},
                    {id: "id1", name: "executorCores", value: dataExecutorCores, description: "executorCores"},
                    {id: "id2", name: "executorMemory", value: dataExecutorMemory, description: "executorMemory"},
                    {id: "id3", name: "executorNumber", value: dataExecutorNumber, description: "executorNumber"},
                    {id: "id4", name: "description", value: dataDescription, description: "description"}
                ];
                add(dataId, addDatas);

                //Remove the timer if successful
                window.clearTimeout(timerPath);
            } else {
                if (!timerPath) {
                    timerPath = window.setTimeout(queryFlowProperty(flowPageId), 500);
                    console.log(3);
                }
                flag++;
                if (flag > 5) {
                    window.clearTimeout(timerPath);
                    return;
                }
            }
            layer.close(layer.index);
        }
    });
}

function queryPathInfo(id) {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/piflow-web/flowGroupPath/queryPathInfoFlowGroup",
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
                    $("#flowNameID").html('pageId：');
                    $("#descriptionID").html('flowGroupName：');
                    $("#driverMemoryID").html('inport：');
                    $("#executorCoresID").html('outport：');
                    $("#executorMemoryID").html('form：');
                    $("#executorNumberID").html('to：');
                    $("#createDateID").html('createTime：');
                    $("#updateFlowNameBtn").hide();
                    document.getElementById('flowNameSpan').innerText = queryInfo.pageId;
                    document.getElementById('flowDescription').innerText = queryInfo.flowGroupVo.name;
                    document.getElementById('flowDriverMemory').innerText = queryInfo.inport;
                    document.getElementById('flowExecutorCores').innerText = queryInfo.outport;
                    document.getElementById('flowExecutorMemory').innerText = queryInfo.flowFrom;
                    document.getElementById('flowExecutorNumber').innerText = queryInfo.flowTo;
                    document.getElementById('flowCreateDate').innerText = queryInfo.crtDttmString;
                    //document.getElementById('table_idDiv').style.display = 'none';
                }
            } else {
                console.log("Path attribute query null");
            }
        }
    });
}

function add(flowId, addDatas) {
    if (flowId && addDatas && addDatas.length > 0 && divValue) {
        var table = document.createElement("table");
        table.style.borderCollapse = "separate";
        table.style.borderSpacing = "0px 5px";
        table.style.marginLeft = "12px";
        table.style.width = "97%";
        var tbody = document.createElement("tbody");
        for (var i = 0; i < addDatas.length; i++) {
            var addData_i = addDatas[i];
            var displayName = document.createElement('input');
            displayName.setAttribute('data-toggle', 'true');
            displayName.setAttribute('class', 'form-control');
            displayName.setAttribute('id', '' + addData_i.id + '');
            displayName.setAttribute('name', '' + addData_i.name + '');
            displayName.setAttribute('onclick', 'stopTabTd(this)');
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

// Add operation processing
function addCellsCustom(cells, operType) {
    var removePaths = [];
    var paths = [];
    for (var i = 0; i < cells.length; i++) {
        var cellfor = cells[i];
        if (cellfor && cellfor.edge) {
            if (cellfor.target && cellfor.source) {
                paths[paths.length] = cellfor;
            } else {
                removePaths[removePaths.length] = cellfor;
            }
        } else if (cellfor.style && (cellfor.style).indexOf("text\;") === 0) {
            removePaths[removePaths.length] = cellfor;
        }
    }
    graphGlobal.removeCells(removePaths);
    if (cells.length != removePaths.length) {
        saveXml(paths, operType);
    }
}

//Save xml file and related information
function saveXml(paths, operType) {
    var getXml = thisEditor.getGraphXml();
    var xml_outer_html = getXml.outerHTML;
    //var waitxml = encodeURIComponent(getXml.outerHTML);//This is the xml code to be submitted to the background.

    $.ajax({
        cache: true,//Keep cached data
        type: "POST",//post request
        url: "/piflow-web/flowGroup/saveFlowGroupData",
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
                thisEditor.setModified(false);
                if (operType && '' !== operType) {
                    //获取port
                    //getStopsPort(paths);
                    //getStopsPortNew(paths);
                }
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

//open xml file
function openXml() {
    $.ajax({
        cache: true,//Keep cached data
        type: "POST",//post request
        url: "/piflow-web/flow/loadData",
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

//load xml file
function loadXml(loadStr) {
    var xml = mxUtils.parseXml(loadStr);
    var node = xml.documentElement;
    var dec = new mxCodec(node.ownerDocument);
    dec.decode(node, graphGlobal.getModel());
    eraseRecord()
}

function queryFlowGroup() {
    $.ajax({
        data: {"load": loadId},
        cache: true,//Keep cached data
        type: "POST",//post request
        url: "/piflow-web/flow/queryFlowGroupData",
        async: true,//Synchronous Asynchronous
        error: function (request) {//Operation after request failure
            return;
        },
        success: function (data) {//After the request is successful
            var dataMap = JSON.parse(data);
            if (document.getElementById("UUID")) {//Js determines whether the element exists
                var flowGroupVo = dataMap.flowGroupVo;
                if (flowGroupVo != null && flowGroupVo != "")
                    if (flowGroupVo != null && flowGroupVo != "") {
                        document.getElementById('UUID').innerText = flowGroupVo.id ? flowGroupVo.id : "No content";
                        document.getElementById('flowGroupName').innerText = flowGroupVo.name ? flowGroupVo.name : "No content";
                        document.getElementById('flowGroupDescription').innerText = flowGroupVo.description ? flowGroupVo.description : "No content";
                        document.getElementById('createTime').innerText = flowGroupVo.crtDttmString ? flowGroupVo.crtDttmString : "No content";
                        document.getElementById('flowQuantity').innerText = flowGroupVo.flowVoList ? flowGroupVo.flowVoList.length : "0";
                    } else {
                        document.getElementById('UUID').innerText = "No content";
                        document.getElementById('flowName').innerText = "No content";
                        document.getElementById('flowDescription').innerText = "No content";
                        document.getElementById('createTime').innerText = "No content";
                        document.getElementById('flowQuantity').innerText = "0";
                    }
                getRunningProcessList();
            }
        }
    });
}

//run
function runFlowGroup(runMode) {
    fullScreen.show();
    console.info("ss");
    var data = {flowGroupId: loadId}
    if (runMode) {
        data.runMode = runMode;
    }
    $.ajax({
        cache: true,//Keep cached data
        type: "POST",//post request
        url: "/piflow-web/flowGroup/runFlowGroup",
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
            //console.log("success");
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                layer.msg(dataMap.errorMsg, {icon: 1, shade: 0, time: 2000}, function () {
                    //Jump to the monitoring page after starting successfully
                    var windowOpen = window.open("/piflow-web/processGroup/getProcessGroupById?parentAccessPath=groupDrawingBoard&processGroupId=" + dataMap.processGroupId);
                    //var tempwindow = window.open('_blank');
                    if (windowOpen == null || typeof (windowOpen) == 'undefined') {
                        alert('The window cannot be opened. Please check your browser settings.')
                    }
                    //tempwindow.location = "/piflow-web/processGroup/getProcessGroupById?parentAccessPath=grapheditor&processGroupId=" + dataMap.processGroupId;
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

function saveFlowGroupTemplate() {
    var getXml = thisEditor.getGraphXml();
    var xml_outer_html = getXml.outerHTML;
    layer.prompt({
        title: 'please enter the template name',
        formType: 0,
        btn: ['submit', 'cancel']
    }, function (text, index) {
        layer.close(index);
        $.ajax({
            cache: true,//Keep cached data
            type: "POST",//post request
            url: "/piflow-web/flowGroupTemplate/saveFlowGroupTemplate",
            data: {
                value: xml_outer_html,
                load: loadId,
                name: text
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

function uploadFlowGroupTemplateBtn() {
    document.getElementById("flowGroupTemplateFile").click();
}

function uploadFlowGroupTemplate() {
    if (!FileTypeCheck()) {
        return false;
    }
    var formData = new FormData($('#uploadForm')[0]);
    $.ajax({
        type: 'post',
        url: "/piflow-web/flowGroupTemplate/uploadXmlFile",
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

function FileTypeCheck() {
    var obj = document.getElementById('flowGroupTemplateFile');
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

function loadingXml(id, loadId) {
    fullScreen.show();
    $.ajax({
        type: 'post',
        data: {
            templateId: id,
            load: loadId
        },
        async: true,
        url: "/piflow-web/flowGroupTemplate/loadingXmlPage",
    }).success(function (data) {
        window.location.reload();
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
    $.ajax({
        url: "/piflow-web/flowGroupTemplate/flowGroupTemplateAllSelect",
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
                        + '<input type="button" class="btn" value="Submit" onclick="loadFlowGroupTemplate()"/>'
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

function loadFlowGroupTemplate() {
    var id = $("#loadingXmlSelectNew").val();
    if (id == '-1') {
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

function processListener(evt, operType) {
    if (!isExample) {
        if ('ADD' === operType) {
            var cells = evt.properties.cells;
            addCellsCustom(cells, 'ADD');
            if ('cellsAdded' == evt.name) {
                findBasicInfo(evt);
            }
        } else if ('MOVED' === operType) {
            if (evt.properties.disconnect) {
                saveXml(null, operType);
            }
            findBasicInfo(evt);
        } else if ('REMOVED' === operType) {
            saveXml(null, operType);
        }

    } else {
        if ('ADD' === operType || 'REMOVED' === operType) {
            layer.msg("This is an example, you can't add, edit or delete", {
                icon: 2,
                shade: 0,
                time: 2000
            }, function () {
            });
        } else if ('MOVED' === operType) {
            findBasicInfo(evt);
        }
        prohibitEditing(isExample, operType);
    }

}

function prohibitEditing(isExample, operType) {
    $.ajax({
        cache: true,//Keep cached data
        type: "POST",//post request
        url: "/piflow-web/exampleMenu/exampleUrlList",
        data: {},
        async: true,
        error: function (request) {//Operation after request failure
            if ('ADD' === operType || 'REMOVED' === operType) {
                location.reload();
            }
            eraseRecord()
            return;
        },
        success: function (data) {//After the request is successful
            if ('ADD' === operType || 'REMOVED' === operType) {
                location.reload();
            }
            eraseRecord()
        }
    });
}

function getRunningProcessList() {
    $.ajax({
        cache: true,//Keep cached data
        type: "POST",//post request
        url: "/piflow-web/grapheditor/getRunningProcessList",
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

//Erase artboard records
function eraseRecord() {
    thisEditor.lastSnapshot = new Date().getTime();
    thisEditor.undoManager.clear();
    thisEditor.ignoredChanges = 0;
    thisEditor.setModified(false);
}

function getFlowList() {
    var window_width = $(window).width();//Get browser window width
    var window_height = $(window).height();//Get browser window height
    $.ajax({
        type: "POST",//Request type post
        url: "/piflow-web/flow/getFlowListHtml",//This is the name of the file where I receive data in the background.
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