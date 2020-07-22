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


function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}

function addMxCellOperation(evt) {
    var cells = evt.properties.cells;
    statusgroup = cells[0].value;
    groupGraphAddCells(cells);
    if ('cellsAdded' == evt.name) {
        findBasicInfo(evt);
    }
}


function removeMxCellOperation(evt) {
    saveXml(null, 'REMOVED');
}

function groupGraphAddCells(cells) {
    var removeCellArray = [];
    var addCellArray = [];
    var addPathArray = [];
    cells.forEach(cellFor => {
        if (cellFor && cellFor.edge) {
            var cellForSource = cellFor.source;
            var cellForTarget = cellFor.target;
            if (cellForSource && cellForTarget
                && (cellForSource.style && (cellForSource.style).indexOf("text\;") !== 0)
                && (cellForTarget.style && (cellForTarget.style).indexOf("text\;") !== 0)) {
                var addCell = graphCellToMxCellVo(cellFor);
                if (addCell) {
                    addCellArray.push(addCell);
                }
                addPathArray.push(cellFor);
            } else {
                removeCellArray.push(cellFor);
            }
        } else if (cellFor.style && (cellFor.style).indexOf("image\;") === 0) {
            var addCell = graphCellToMxCellVo(cellFor);
            if (addCell) {
                addCellArray.push(addCell);
            }
            if (!removegroupPaths) {
                removegroupPaths = [];
            }
            removegroupPaths.push(cellFor);
        } else {
            var addCell = graphCellToMxCellVo(cellFor);
            if (addCell) {
                addCellArray.push(addCell);
            }
        }
    });
    graphGlobal.removeCells(removeCellArray);
    if (cells.length != removeCellArray.length) {
        var time, time1;
        ajaxRequest({
            cache: true,//Keep cached data
            type: "POST",//Request type post
            url: "/mxGraph/addMxCellAndData",
            data: JSON.stringify({
                mxCellVoList: addCellArray,
                loadId: loadId
            }),
            //contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
            contentType: 'application/json;charset=utf-8',
            async: true,//Synchronous Asynchronous
            error: function (request) {//Operation after request failure
                layer.msg('Add failed, refresh page after 1 second', {icon: 2, shade: 0, time: 2000}, function () {
                    window.location.reload();
                });
                return;
            },
            success: function (data) {//After the request is successful
                var dataMap = JSON.parse(data);
                if (200 === dataMap.code) {
                    //console.log(operType + " save success");
                    console.log("Add save success");
                    if (statusgroup == "group") {
                        $("#flowGroupId").val("");
                        $("#flowGroupName").val("");
                        $("#description1").val("");
                        layer.open({
                            type: 1,
                            title: '<span style="color: #269252;">create flow group</span>',
                            shadeClose: false,
                            shade: 0.3,
                            closeBtn: 1,
                            shift: 7,
                            area: ['580px', '520px'], //Width height
                            skin: 'layui-layer-rim', //Add borders
                            content: $("#SubmitPage"),
                            success: function () {
                                $(".layui-layer-page").css("z-index", "1998910151");

                                queryFlowOrFlowGroupProperty(flowsPagesId);

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
                                                // layer.closeAll()
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
                    } else if (statusgroup == "flow") {
                        $("#flowId").val("");
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
                            closeBtn: 1,
                            shift: 7,
                            area: ['580px', '520px'], //Width height
                            skin: 'layui-layer-rim', //Add borders
                            content: $("#SubmitPageFlow"),
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
                                                // layer.closeAll()
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

                    } else if (statusgroup == null || statusgroup == "" || 'TASK' === Format.customizeType) {


                    } else {

                        if (graphGlobal.isEnabled()) {
                            graphGlobal.startEditingAtCell();
                        }
                    }
                    thisEditor.setModified(false);
                    //获取port
                    //getStopsPort(paths);
                    if ('TASK' === Format.customizeType) {
                        getStopsPortNew(addPathArray);
                    }
                } else {
                    layer.msg("Add save fail", {icon: 2, shade: 0, time: 2000}, function () {
                    });
                    console.log("Add save fail");
                    fullScreen.hide();
                }

            }

        });
    }
}

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

//Double-click monitoring events
function OpenTheMonitorArtboard(evt) {
    var cellFor = evt.properties.cell;
    var processGroupId = getQueryString("load")
    if (cellFor.style && (cellFor.style).indexOf("text\;") === 0) {
    } else {
        ajaxRequest({
            cache: true,//Keep cached data
            type: "POST",//Request type post
            url: "/processGroup/getProcessIdByPageId",//This is the name of the file where I receive data in the background.
            data: {
                processGroupId: processGroupId,
                pageId: cellFor.id
            },
            async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
            error: function (request) {//Operation after request failure
                layer.msg(' failed', {icon: 2, shade: 0, time: 1000});
                return;
            },
            success: function (data) {//Operation after request successful
                if (data) {
                    var dataMap = JSON.parse(data);
                    if (200 === dataMap.code) {
                        var tempWindow = window.location.href = "/piflow-web/mxGraph/drawingBoard?drawingBoardType=PROCESS&parentAccessPath=processGroupList&processType=PROCESS_GROUP&load=" + dataMap.processGroupId;
                        ;
                        // var tempWindow = window.open(urlPath, "_blank",);
                        if (tempWindow == null || typeof (tempWindow) == 'undefined') {
                            alert('The window cannot be opened. Please check your browser settings.')
                        }
                    }
                } else {
                    layer.msg(' failed', {icon: 2, shade: 0, time: 1000});
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
            //When you add a stop to the drawing board for the first time, the drag process does not get the id.
            queryFlowOrFlowGroupProperty(maxFlowPageId);
        }
    }
    if (typeof (cells) != "undefined" && null != id && "" != id && "null" != id) {
        if (document.getElementById("customizeBasic_td_1_2_input1_id"))
            document.getElementById('customizeBasic_td_1_2_input1_id').value = value;
        //Check the line is the case
        if (cells.cell && cells.cell.edge) {
            if (cells.cell.target && cells.cell.source) {
                //Query Path
                queryPathInfo(id);
            }
        } else {
            //Query attribute information;
            queryFlowOrFlowGroupProperty(id);
        }
    }
}

function queryStopsProperty(stopPageId) {
    ajaxRequest({
        cache: true,
        type: "POST",
        url: "/stops/queryIdInfo",
        data: {"stopPageId": stopPageId, "fid": loadId},
        async: true,
        error: function (request) {
            //alert("Jquery Ajax request error!!!");
            return;
        },
        success: function (data) {
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                var stopsVoData = dataMap.stopsVo;
                var addParamData = {
                    data: stopsVoData.propertiesVo,
                    stopId: stopsVoData.id,
                    isCheckpoint: stopsVoData.isCheckpoint,
                    stopPageId: stopPageId,
                    isCustomized: stopsVoData.isCustomized,
                    stopsCustomizedPropertyVoList: stopsVoData.stopsCustomizedPropertyVoList,
                    stopOutPortType: stopsVoData.outPortType,
                    dataSourceVo: stopsVoData.dataSourceVo
                };
                getImagesType(data, "TASK")
                add(addParamData);
                //  $("#customizeBasic_td_1_2_input2_id").data("result",evt);
                $('#customizeBasic_td_1_2_span_id').text(stopsVoData.name);
                $('#customizeBasic_td_1_2_input1_id').attr("value", stopsVoData.name);
                $('#customizeBasic_td_1_2_input1_id').attr("name", stopsVoData.id);
                $('#customizeBasic_td_1_2_input2_id').attr("value", stopsVoData.name);
                $('#customizeBasic_td_1_2_input2_id').attr("name", stopsVoData.pageId);
                $('#customizeBasic_td_2_2_span_id').text(stopsVoData.description);
                $('#customizeBasic_td_3_2_label_id').text(stopsVoData.groups);
                $('#customizeBasic_td_4_2_label_id').text(stopsVoData.bundel);
                $('#customizeBasic_td_5_2_label_id').text(stopsVoData.version);
                $('#customizeBasic_td_6_2_label_id').text(stopsVoData.owner);
                $('#customizeBasic_td_7_2_label_id').text(stopsVoData.crtDttmString);
                var oldPropertiesVo = stopsVoData.oldPropertiesVo;
                if (oldPropertiesVo && oldPropertiesVo.length > 0) {
                    var table = document.createElement("table");
                    table.style.borderCollapse = "separate";
                    table.style.borderSpacing = "0px 5px";
                    table.style.marginLeft = "12px";
                    table.style.width = "97%";
                    var tbody = document.createElement("tbody");
                    for (var y = 0; y < oldPropertiesVo.length; y++) {
                        var select = document.createElement('select');
                        //select.style.width = "290px";
                        select.style.height = "32px";
                        select.setAttribute('id', 'old_' + oldPropertiesVo[y].name + '');
                        select.setAttribute('class', 'form-control');
                        select.setAttribute('disabled', 'disabled');
                        var displayName = oldPropertiesVo[y].displayName;
                        var customValue = oldPropertiesVo[y].customValue;
                        var allowableValues = oldPropertiesVo[y].allowableValues;
                        var isSelect = oldPropertiesVo[y].isSelect;
                        //Is it required?
                        var required = oldPropertiesVo[y].required;
                        //If it is greater than 4 and isSelect is true, there is a drop-down box
                        if (allowableValues.length > 4 && isSelect) {
                            var selectValue = JSON.parse(allowableValues);
                            var selectInfo = JSON.stringify(selectValue);
                            var strs = selectInfo.split(",");
                            var optionDefault = document.createElement("option");
                            optionDefault.value = '';
                            optionDefault.innerHTML = '';
                            optionDefault.setAttribute('selected', 'selected');
                            select.appendChild(optionDefault);
                            //Loop to assign value to select
                            for (i = 0; i < strs.length; i++) {
                                var option = document.createElement("option");
                                option.style.backgroundColor = "#DBDBDB";
                                option.value = strs[i].replace("\"", "").replace("\"", "").replace("\[", "").replace("\]", "");
                                option.innerHTML = strs[i].replace("\"", "").replace("\"", "").replace("\[", "").replace("\]", "");
                                //Sets the default selection
                                if (strs[i].indexOf('' + customValue + '') != -1) {
                                    option.setAttribute('selected', 'selected');
                                }
                                select.appendChild(option);
                            }
                        }
                        var displayName = document.createElement('input');
                        if (required)
                            displayName.setAttribute('data-toggle', 'true');
                        displayName.setAttribute('class', 'form-control');
                        displayName.setAttribute('id', 'old_' + oldPropertiesVo[y].id + '');
                        displayName.setAttribute('name', '' + oldPropertiesVo[y].name + '');
                        displayName.setAttribute('locked', oldPropertiesVo[y].isLocked);
                        // displayName.style.width = "290px";
                        displayName.setAttribute('readonly', 'readonly');
                        displayName.style.cursor = "pointer";
                        displayName.style.background = "rgb(245, 245, 245)";
                        customValue = customValue == 'null' ? '' : customValue;
                        displayName.setAttribute('value', '' + customValue + '');
                        var spanDisplayName = 'span' + oldPropertiesVo[y].displayName;
                        var spanDisplayName = document.createElement('span');
                        var spanFlag = document.createElement('span');
                        spanFlag.setAttribute('style', 'color:red');
                        mxUtils.write(spanDisplayName, '' + oldPropertiesVo[y].name + '' + ": ");
                        mxUtils.write(spanFlag, '*');
                        //Port uneditable
                        if ("outports" == oldPropertiesVo[y].displayName || "inports" == oldPropertiesVo[y].displayName) {
                            displayName.setAttribute('disabled', 'disabled');
                        }

                        var img = document.createElement("img");
                        img.setAttribute('src', '/piflow-web/img/descIcon.png');
                        img.style.cursor = "pointer";
                        img.setAttribute('title', '' + oldPropertiesVo[y].description + '');
                        var tr = document.createElement("tr");
                        tr.setAttribute('class', 'trTableStop');
                        var td = document.createElement("td");
                        td.style.width = "60px";
                        var td1 = document.createElement("td");
                        var td2 = document.createElement("td");
                        var td3 = document.createElement("td");
                        td3.style.width = "25px";
                        //Appendchild () appends elements
                        td.appendChild(spanDisplayName);
                        td3.appendChild(img);
                        //This loop is greater than 4 append drop-down, less than 4 default text box
                        if (allowableValues.length > 4 && isSelect) {
                            td1.appendChild(select);
                        } else {
                            td1.appendChild(displayName);
                            if (required) {
                                td2.appendChild(spanFlag);
                            }
                        }
                        tr.appendChild(td);
                        tr.appendChild(td3);
                        tr.appendChild(td1);
                        tr.appendChild(td2);
                        tbody.appendChild(tr);
                        table.appendChild(tbody);
                    }
                    var old_data_div = '<div id="del_last_reload_div" style="line-height: 27px;margin-left: 10px;font-size: 20px;">'
                        + '<span>last reload data</span>'
                        + '<button class="btn" style="margin-left: 2px;" onclick="deleteLastReloadData(\'' + stopsVoData.id + '\')"><i class="icon-trash"></i></button>'
                        + '</div>';
                    table.setAttribute('id', 'del_last_reload_table');
                    var attributeInfoDivObj = $("#isCheckpoint").parent();
                    attributeInfoDivObj.append(old_data_div);
                    attributeInfoDivObj.append(table);
                    attributeInfoDivObj.append("<hr>");
                }

                //Remove the timer if successful
                window.clearTimeout(timerPath);
            } else {
                //STOP attribute query null
                //console.log("STOP attribute query null");
                if (!timerPath) {
                    timerPath = window.setTimeout(queryStopsProperty(stopPageId), 500);
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

function add(addParamData, flowId, nodeType) {
    if ('TASK' === Format.customizeType) {
        taskAdd(addParamData);
    } else if ('GROUP' === Format.customizeType) {
        groupAdd(addParamData, flowId, nodeType)
    }
}

function taskAdd(addParamData) {
    //function add(data, stopId, isCheckpoint, stopPageId, isCustomized, stopsCustomizedPropertyVoList, stopOutPortType, dataSourceVo) {
    if (addParamData.data && addParamData.data != null && addParamData.data.length > 0) {
        var data = addParamData.data
        while (divValue.hasChildNodes()) {
            divValue.removeChild(divValue.firstChild);
        }
        var table = document.createElement("table");
        table.style.borderCollapse = "separate";
        table.style.borderSpacing = "0px 5px";
        table.style.marginLeft = "12px";
        table.style.width = "97%";
        var tbody = document.createElement("tbody");
        for (var y = 0; y < data.length; y++) {
            var select = document.createElement('select');
            //select.style.width = "290px";
            select.style.height = "32px";
            select.setAttribute('id', '' + data[y].name + '');
            select.setAttribute('onblur', 'shiqu("' + data[y].id + '","' + data[y].name + '","select")');
            select.setAttribute('class', 'form-control');
            if (isExample) {
                select.setAttribute('disabled', 'disabled');
            }
            var displayName = data[y].displayName;
            var customValue = data[y].customValue;
            var allowableValues = data[y].allowableValues;
            var isSelect = data[y].isSelect;
            //Is it required?
            var required = data[y].required;
            //If it is greater than 4 and isSelect is true, there is a drop-down box
            if (allowableValues.length > 4 && isSelect) {
                var selectValue = JSON.parse(allowableValues);
                var selectInfo = JSON.stringify(selectValue);
                var strs = selectInfo.split(",");
                var optionDefault = document.createElement("option");
                optionDefault.value = '';
                optionDefault.innerHTML = '';
                optionDefault.setAttribute('selected', 'selected');
                select.appendChild(optionDefault);
                //Loop to assign value to select
                for (i = 0; i < strs.length; i++) {
                    var option = document.createElement("option");
                    option.style.backgroundColor = "#DBDBDB";
                    option.value = strs[i].replace("\"", "").replace("\"", "").replace("\[", "").replace("\]", "");
                    option.innerHTML = strs[i].replace("\"", "").replace("\"", "").replace("\[", "").replace("\]", "");
                    //Sets the default selection
                    if (strs[i].indexOf('' + customValue + '') != -1) {
                        option.setAttribute('selected', 'selected');
                    }
                    select.appendChild(option);
                }
            }
            var displayName = document.createElement('input');
            if (required)
                displayName.setAttribute('data-toggle', 'true');
            displayName.setAttribute('class', 'form-control');
            displayName.setAttribute('id', '' + data[y].id + '');
            displayName.setAttribute('name', '' + data[y].name + '');
            displayName.setAttribute('onclick', 'attributeTabTd(this,false,null)');
            displayName.setAttribute('locked', data[y].isLocked);
            // displayName.style.width = "290px";
            displayName.setAttribute('readonly', 'readonly');
            displayName.style.cursor = "pointer";
            displayName.style.background = "rgb(245, 245, 245)";
            customValue = customValue == 'null' ? '' : customValue;
            displayName.setAttribute('value', '' + customValue + '');
            var spanDisplayName = 'span' + data[y].displayName;
            var spanDisplayName = document.createElement('span');
            var spanFlag = document.createElement('span');
            spanFlag.setAttribute('style', 'color:red');
            mxUtils.write(spanDisplayName, '' + data[y].name + '' + ": ");
            mxUtils.write(spanFlag, '*');
            //Port uneditable
            if ("outports" == data[y].displayName || "inports" == data[y].displayName) {
                displayName.setAttribute('disabled', 'disabled');
            }

            var img = document.createElement("img");
            img.setAttribute('src', '/piflow-web/img/descIcon.png');
            img.style.cursor = "pointer";
            img.setAttribute('title', '' + data[y].description + '');
            var tr = document.createElement("tr");
            tr.setAttribute('class', 'trTableStop');
            var td = document.createElement("td");
            td.style.width = "60px";
            var td1 = document.createElement("td");
            var td2 = document.createElement("td");
            var td3 = document.createElement("td");
            td3.style.width = "25px";
            //Appendchild () appends elements
            td.appendChild(spanDisplayName);
            td3.appendChild(img);
            //This loop is greater than 4 append drop-down, less than 4 default text box
            if (allowableValues.length > 4 && isSelect) {
                td1.appendChild(select);

            } else {
                td1.appendChild(displayName);
                if (required)
                    td2.appendChild(spanFlag);
            }
            tr.appendChild(td);
            tr.appendChild(td3);
            tr.appendChild(td1);
            tr.appendChild(td2);
            tbody.appendChild(tr);
            table.appendChild(tbody);
        }
        var btn = mxUtils.button('submit', mxUtils.bind(this, function (evt) {
            //Create an array
            var arrayObj = new Array();
            for (var y = 0; y < data.length; y++) {
                var content = document.getElementById('' + data[y].displayName + '').value;
                var classname = document.getElementById('' + data[y].displayName + '').className;
                if (content != null && content.length > 0) {
                    arrayObj.push(content + "#id#" + data[y].id);
                } else {
                    if (classname == 'true') {
                        $("#" + data[y].displayName + "").focus();
                        $("#" + data[y].displayName + "").css("background-color", "#FFD39B");
                    }
                }
            }
            ajaxRequest({
                cache: true,
                type: "POST",
                url: "/stops/updateStops",
                data: {content: arrayObj},
                async: true,
                traditional: true,
                error: function (request) {
                    console.log("attribute update error");
                    return;
                },
                success: function (data) {
                    console.log("attribute update success");
                    $("textarea").blur();
                    $("select").blur();
                }
            });
        }));
        var checkboxCheckpoint = document.createElement('input');
        checkboxCheckpoint.setAttribute('type', 'checkbox');
        checkboxCheckpoint.setAttribute('id', 'isCheckpoint');
        if (addParamData.isCheckpoint) {
            checkboxCheckpoint.setAttribute('checked', 'checked');
        }
        if (isExample) {
            checkboxCheckpoint.setAttribute('disabled', 'disabled');
        }
        checkboxCheckpoint.setAttribute('onclick', 'saveCheckpoints("' + addParamData.stopId + '")');
        var spanCheckpoint = document.createElement('span');
        mxUtils.write(spanCheckpoint, 'Whether to add Checkpoint');
        btn.style.width = '202px';
        btn.style.marginLeft = '18px';
        btn.style.marginTop = '10px';


        var dataSource = '<table style="border-collapse: separate; border-spacing: 0px 5px; margin-left: 12px; width: 97%;">'
            + '<tbody>'
            + '<tr>'
            + '<td style="width: 99px;"><span>dataSource: </span></td>'
            + '<td style="width: 25px;"><img src="/piflow-web/img/descIcon.png" title="Fill Datasoure" style="cursor: pointer;"></td>'
            + '<td>'
            // + '<select id="datasourceSelectElement" onblur="alert(1);" class="form-control" style="height: 32px;">'
            + '<div id="datasourceDivElement" style="float: left;width: 98%;">'
            + '<select id="datasourceSelectElement" class="form-control" style="height: 32px;">'
            + '<option value="" selected="selected"></option>'
            + '</select>'
            + '</div>'
            + '<div style="float: left;width: 2%;">'
            + '<button class="btn" onclick="openDatasourceList()" style="margin-left: 2px;"><i class="glyphicon glyphicon-edit"></i></button>'
            + '</div>'
            + '</td>'
            + '<td style="width: 37px;text-align: right;">'
            + ''
            + '</td>'
            + '</tr>'
            + '</tbody>'
            + '</table>'
            + '<hr>';

        $(divValue).append(dataSource);
        divValue.appendChild(table);
        //divValue.appendChild(btn);
        divValue.appendChild(checkboxCheckpoint);
        divValue.appendChild(spanCheckpoint);
        $(divValue).append('<hr>');
        getDatasourceList(addParamData.stopId, addParamData.stopPageId, addParamData.dataSourceVo);
    }
    if (addParamData.isCustomized) {
        var div_obj_fill = $("#fill_datasource_id");
        if (!div_obj_fill.html()) {
            $(divValue).append('<div id="fill_datasource_id"></div>');
            div_obj_fill = $("#fill_datasource_id");
        }
        $("#customized_id").remove();
        var customizedTableObj = $("#customizedTableObj").clone();
        var tr_all = "";
        if (addParamData.stopsCustomizedPropertyVoList && addParamData.stopsCustomizedPropertyVoList.length > 0) {
            var stopsCustomizedPropertyVoList = addParamData.stopsCustomizedPropertyVoList;
            for (var i = 0; i < stopsCustomizedPropertyVoList.length; i++) {
                tr_all += setCustomizedTableHtml(addParamData.stopPageId, stopsCustomizedPropertyVoList[i], addParamData.stopOutPortType);
            }
        }
        customizedTableObj.find("a").attr("href", "javascript:openAddStopCustomAttrPage('" + addParamData.stopId + "');")
        customizedTableObj.find(".trTableCustomizedStopProperty").remove();
        customizedTableObj.find("tbody").append(tr_all);
        var customized_obj = '<div id="customized_id">' + customizedTableObj.html() + '</div>';
        div_obj_fill.before(customized_obj);
    }
}

function groupAdd(addParamData, flowId, nodeType) {
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

function setCustomizedTableHtml(stopPageId, stopsCustomizedPropertyVo, stopOutPortType) {
    var isRouter = false;
    if (stopOutPortType && stopOutPortType.stringValue === "ROUTE") {
        isRouter = true;
    }
    var table_tr = "";
    if (stopsCustomizedPropertyVo) {
        table_tr = '<tr class="trTableCustomizedStopProperty">'
            + '<td style="width: 60px;">'
            + '<span style="margin-left: 10px;">' + stopsCustomizedPropertyVo.name + ': </span>'
            + '</td>'
            + '<td style="width: 25px;">'
            + '<img src="/piflow-web/img/descIcon.png" title="' + stopsCustomizedPropertyVo.description + '" style="cursor: pointer;">'
            + '</td>'
            + '<td>'
            + '<input data-toggle="true"class="form-control"'
            + 'id="' + stopsCustomizedPropertyVo.id + '"'
            + 'name="' + stopsCustomizedPropertyVo.name + '" '
            + 'value="' + stopsCustomizedPropertyVo.customValue + '" '
            + 'onclick="attributeTabTd(this,true,null)"readonly="readonly" value=""style="background: rgb(245, 245, 245);">'
            + '</td>'
            + '<td>'
            + '<span style="color:red">*</span>'
            + '<a class="btn" href="javascript:removeStopCustomProperty(\'' + stopPageId + '\',\'' + stopsCustomizedPropertyVo.id + '\',' + isRouter + ');"><i class="glyphicon glyphicon-remove" style="color: red;"></i></a>'
            + '</td>'
            + '</tr>';
    }
    return table_tr;

}

function getDatasourceList(stop_id, stop_page_id, dataSourceVo) {
    ajaxRequest({
        cache: true,//sava cache data
        type: "POST",// request type
        url: "/datasource/getDatasourceList",

        //data:$('#loginForm').serialize(),//Form serialization
        async: true,//open asynchronous request
        error: function (request) {//Operation after request failure
            return;
        },
        success: function (data) {//Operation after request successful
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                var dataSourceList = dataMap.data;
                var select_html = '<select id="datasourceSelectElement" class="form-control" style="width: 100%;" onchange="fillDatasource(this,\'' + stop_id + '\',\'' + stop_page_id + '\')">'
                if (dataSourceList && dataSourceList.length > 0) {
                    var option_html = '<option value="">please select datasource...</option>';
                    var dataSourceVoId = '';
                    if (dataSourceVo && dataSourceVo.id) {
                        dataSourceVoId = dataSourceVo.id;
                    }
                    if ('' === dataSourceVoId) {
                        option_html = '<option selected="selected" value="">please select datasource...</option>';
                    }
                    for (var i = 0; i < dataSourceList.length; i++) {
                        if (dataSourceList[i].id === dataSourceVoId) {
                            option_html += ('<option selected="selected" value="' + dataSourceList[i].id + '">');
                        } else {
                            option_html += ('<option value="' + dataSourceList[i].id + '">');
                        }
                        option_html += (dataSourceList[i].dataSourceName + '(' + dataSourceList[i].dataSourceType + ')'
                            + '</option>');
                    }
                }
                select_html += (option_html + "</select></div>");
                $('#datasourceDivElement').html(select_html);
            } else {
                //alert(operType + " save fail");
                layer.msg("Load fail", {icon: 2, shade: 0, time: 2000}, function () {
                });
                console.log("Load fail");
            }
        }
    });
}

function fillDatasource(datasource, stop_id, stop_page_id) {
    var datasourceId = $(datasource).val();
    if (stop_id) {
        ajaxRequest({
            cache: true,//Keep cached data
            type: "POST",//Request type post
            url: "/datasource/fillDatasource",//This is the name of the file where I receive data in the background.
            //data:$('#loginForm').serialize(),//Serialize the form
            data: {"dataSourceId": datasourceId, "stopId": stop_id},
            async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
            error: function (request) {//Operation after request failure
                return;
            },
            success: function (data) {//Operation after request successful
                var dataMap = JSON.parse(data);
                if (200 === dataMap.code) {
                    queryStopsProperty(stop_page_id);
                } else {
                    //alert(operType + " save fail");
                    layer.msg(dataMap.errorMsg, {icon: 2, shade: 0, time: 2000}, function () {
                    });
                    console.log(dataMap.errorMsg);
                }
            }
        });
    } else {
        layer.msg("failed, stopId is null or datasourceId is null", {icon: 2, shade: 0, time: 2000}, function () {
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
                    $("#description1").val("");
                    layer.open({
                        type: 1,
                        title: '<span style="color: #269252;">create flow group</span>',
                        shadeClose: false,
                        shade: 0.3,
                        closeBtn: 1,
                        shift: 7,
                        area: ['580px', '520px'], //Width height
                        skin: 'layui-layer-rim', //Add borders
                        content: $("#SubmitPage"),
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
                                            // layer.closeAll()
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
                        content: $("#SubmitPageFlow"),
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
                                            // layer.closeAll()
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

                } else if (statusgroup == null || statusgroup == "" || 'TASK' === Format.customizeType) {


                } else {

                    if (graphGlobal.isEnabled()) {
                        graphGlobal.startEditingAtCell();
                    }
                }
                thisEditor.setModified(false);
                if (operType && '' !== operType) {
                    //获取port
                    //getStopsPort(paths);
                    if ('TASK' === Format.customizeType) {
                        getStopsPortNew(paths);
                    }
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

function cancelFlow() {
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

function saveOrUpdateFlowGroup() {
    if (flowGroupdata == undefined) {
        alert("Please click the close button and drag it again to create")
        return
    }
    var id = $("#flowGroupId").val();
    var flowGroupName = $("#flowGroupName").val();
    var description = $("#description1").val();
    if (!checkGroupInput(flowGroupName)) {
        // layer.closeAll()
        // layer.msg('flowName Can not be empty', {icon: 2, shade: 0, time: 2000});
        alert("flowName Can not be empty")
    } else {
        // if (flowGroupdata == undefined || "") {
        //     layer.closeAll()
        //     layer.msg("Network Anomaly", {icon: 5})
        // } else {
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

//弹框------------------end---------------------------

//flow Information popup-----
function saveFlow() {

    if (flowdatas == undefined) {
        alert("Please click the close button and drag it again to create")
        return
    }
    // queryFlowOrFlowGroupProperty(flowsPagesId)
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
        // if (flowdatas == undefined || "") {
        //     layer.closeAll()
        //     layer.msg("Network Anomaly", {icon: 5})
        // } else {

        var requestDataParam = {
            flowGroupId: loadId,
            flowId: flowdatas.id,
            pageId: flowdatas.pageId,
            updateType: "flow",
            name: flowName
        };
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
                var msgtrue = false
                if (200 === dataMap.code) {
                    //reload xml
                    var xml = mxUtils.parseXml(dataMap.XmlData);
                    var node = xml.documentElement;
                    var dec = new mxCodec(node.ownerDocument);
                    dec.decode(node, graphGlobal.getModel());
                    $("#customizeBasic_td_1_2_input2_id").val(flowName);
                    layer.msg(dataMap.errorMsg, {icon: 1, shade: 0, time: 2000}, function () {
                        // findBasicInfo(results);
                    });
                    layer.closeAll()
                } else {
                    // layer.msg(dataMap.errorMsg, {icon: 2, shade: 0, time: 2000}, function () {
                    // });
                    alert(dataMap.errorMsg)
                    msgtrue = false

                }

                ajaxRequest({
                    cache: true,
                    type: "POST",
                    url: "/flow/updateFlowBaseInfo",
                    data: {
                        id: flowdatas.id,
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
                        if (200 === dataMap.code) {
                            var flowVo = dataMap.flowVo;
                            //baseInfo
                            $('#customizeBasic_td_2_2_span_id').text(flowVo.description);
                            $('#customizeBasic_td_3_2_label_id').text(flowVo.driverMemory);
                            $('#customizeBasic_td_4_2_label_id').text(flowVo.executorCores);
                            $('#customizeBasic_td_5_2_label_id').text(flowVo.executorMemory);
                            $('#customizeBasic_td_6_2_label_id').text(flowVo.executorNumber);
                        } else {
                            layer.msg('', {icon: 2, shade: 0, time: 2000}, function () {
                            });
                        }
                        console.log("attribute update success");
                        if (msgtrue) {
                            layer.closeAll('page');
                        }
                    }
                });

            }
        });
    }
};

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

//load xml file
function loadXml(loadStr) {
    var xml = mxUtils.parseXml(loadStr);
    var node = xml.documentElement;
    var dec = new mxCodec(node.ownerDocument);
    dec.decode(node, graphGlobal.getModel());
    eraseRecord()
}

//Request interface to reload'stops'
function reloadStops() {
    fullScreen.show();
    ajaxRequest({
        data: {"load": loadId},
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/stops/reloadStops",
        error: function (request) {//Operation after request failure
            fullScreen.hide();
            //alert("reload fail");
            layer.msg("reload fail", {icon: 2, shade: 0, time: 2000}, function () {
            });
            return;
        },
        success: function (data) {//Operation after request successful
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                window.location.href = "/piflow-web/mxGraph/drawingBoard?drawingBoardType=TASK&load=" + dataMap.load + "&_" + new Date().getTime();
            } else {
                //alert("reload fail");
                layer.msg("reload fail", {icon: 2, shade: 0, time: 2000}, function () {
                });
                fullScreen.hide();
            }
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

//get port
function getStopsPortNew(paths) {
    if (paths && paths.length > 0) {
        pathsCells = paths;
        if (pathsCells.length > 1) {
            graphGlobal.removeCells(pathsCells);
        } else {
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
            ajaxRequest({
                cache: true,
                type: "get",
                url: "/stops/getStopsPort",
                data: {
                    "flowId": loadId,
                    "sourceId": sourceMxCellId,
                    "targetId": targetMxCellId,
                    "pathLineId": pathLineId
                },
                async: true,
                traditional: true,
                error: function (request) {
                    console.log("error");
                    return;
                },
                success: function (data) {
                    var dataMap = JSON.parse(data);
                    if (200 === dataMap.code) {
                        var showHtml = $('#portShowDiv').clone();
                        showHtml.find('#protInfo1Copy').attr('id', 'protInfoR_R');
                        showHtml.find('#sourceTitle1Copy').attr('id', 'sourceTitleR_R');
                        showHtml.find('#sourceTitleStr1Copy').attr('id', 'sourceTitleStrR_R');
                        showHtml.find('#sourceTitleCheckbox1Copy').attr('id', 'sourceTitleCheckboxR_R');
                        showHtml.find('#sourceTitleBtn1Copy').attr('id', 'sourceTitleBtnR_R');
                        showHtml.find('#sourceCrtPortId1Copy').attr('id', 'sourceCrtPortIdR_R');
                        showHtml.find('#sourceCrtPortBtnId1Copy').attr('id', 'sourceCrtPortBtnIdR_R');
                        showHtml.find('#sourceTypeDiv1Copy').attr('id', 'sourceTypeDivR_R');
                        showHtml.find('#sourceRouteFilterList1Copy').attr('id', 'sourceRouteFilterListR_R');
                        showHtml.find('#sourceRouteFilterSelect1Copy').attr('id', 'sourceRouteFilterSelectR_R');
                        showHtml.find('#targetTitle1Copy').attr('id', 'targetTitleR_R');
                        showHtml.find('#targetTitleStr1Copy').attr('id', 'targetTitleStrR_R');
                        showHtml.find('#targetTitleCheckbox1Copy').attr('id', 'targetTitleCheckboxR_R');
                        showHtml.find('#targetTitleBtn1Copy').attr('id', 'targetTitleBtnR_R');
                        showHtml.find('#targetCrtPortId1Copy').attr('id', 'targetCrtPortIdR_R');
                        showHtml.find('#targetCrtPortBtnId1Copy').attr('id', 'targetCrtPortBtnIdR_R');
                        showHtml.find('#targetTypeDiv1Copy').attr('id', 'targetTypeDivR_R');
                        showHtml.find('#targetRouteFilterList1Copy').attr('id', 'targetRouteFilterListR_R');
                        showHtml.find('#targetRouteFilterSelect1Copy').attr('id', 'targetRouteFilterSelectR_R');

                        showHtml.find('#sourceCrtPortBtnIdR_R').attr('onclick', 'crtAnyPort("sourceCrtPortIdR_R",true)');
                        showHtml.find('#targetCrtPortBtnIdR_R').attr('onclick', 'crtAnyPort("targetCrtPortIdR_R",false)');
                        showHtml.find('#targetTitleBtnR_R').hide();
                        showHtml.find('#targetRouteFilterListR_R').hide();
                        showHtml.find('#sourceTitleBtnR_R').hide();
                        showHtml.find('#sourceRouteFilterListR_R').hide();
                        var sourceType = dataMap.sourceType;
                        var targetType = dataMap.targetType;
                        var sourceTypeStr = sourceType.text;
                        var targetTypeStr = targetType.text;
                        //Determine the number of available ports. If the number of available ports is not greater than 0, delete the line directly
                        if (dataMap.sourceCounts > 0 && dataMap.targetCounts > 0) {
                            // Get a detailed use of the source port
                            var sourcePortUsageMap = dataMap.sourcePortUsageMap;
                            if (sourcePortUsageMap) {
                                showHtml.find('#sourceTitleCheckboxR_R').html("");
                                for (portName in sourcePortUsageMap) {
                                    var portNameVal = sourcePortUsageMap[portName];
                                    var portCheckDiv = '<div id="' + portName + 'Checkbox" class="addCheckbox">'
                                        + '<input id="' + portName + '" name="' + portName + '" type="checkbox" class="addCheckbox" value="' + portName + '"' + (!portNameVal ? 'checked="checked" disabled="disabled"' : '') + '/>'
                                        + '<span class="addCheckbox ' + portName + '" disabled="' + !portNameVal + '">' + portName + '</span>'
                                        + '</div>';
                                    showHtml.find('#sourceTitleCheckboxR_R').append(portCheckDiv);
                                }
                                // Add a create button when the type is Any
                                if ('Any' === sourceTypeStr) {
                                    showHtml.find('#sourceTitleBtnR_R').show();
                                } else if ('Route' === sourceTypeStr) {
                                    showHtml.find('#sourceRouteFilterListR_R').show();
                                    if (dataMap.sourceFilter) {
                                        var sourceFilters = dataMap.sourceFilter;
                                        var selectOptionHtml = '<option value="">Please click Select Filter Country</option>';
                                        for (var i = 0; i < sourceFilters.length; i++) {
                                            var sourceFilter = sourceFilters[i];
                                            selectOptionHtml += '<option value="' + sourceFilter.name + '" title="' + sourceFilter.customValue + '">' + sourceFilter.name + '</option>';
                                        }
                                        showHtml.find('#sourceRouteFilterSelectR_R').html(selectOptionHtml);
                                    } else {
                                        showHtml.find('#sourceRouteFilterSelectR_R').parent().html("Route no filter rule");
                                    }
                                }
                            }
                            showHtml.find('#sourceTypeDivR_R').html(sourceTypeStr);
                            showHtml.find('#sourceTitleStrR_R').html('Source:' + dataMap.sourceName);
                            // Gets the detailed use of the target port
                            var targetPortUsageMap = dataMap.targetPortUsageMap;
                            if (targetPortUsageMap) {
                                showHtml.find('#targetTitleCheckboxR_R').html("");
                                for (portName in targetPortUsageMap) {
                                    var portNameVal = targetPortUsageMap[portName];
                                    var portCheckDiv = '<div id="' + portName + 'Checkbox" class="addCheckbox">'
                                        + '<input id="' + portName + '" name="' + portName + '" type="checkbox" class="addCheckbox" value="' + portName + '"' + (!portNameVal ? 'checked="checked" disabled="disabled"' : '') + '/>'
                                        + '<span class="addCheckbox ' + portName + '" disabled="' + !portNameVal + '">' + portName + '</span>'
                                        + '</div>';
                                    showHtml.find('#targetTitleCheckboxR_R').append(portCheckDiv);
                                }
                                // Add a create button when the type is Any
                                if ('Any' === targetTypeStr) {
                                    showHtml.find('#targetTitleBtnR_R').show();
                                } else if ('Route' === targetTypeStr) {
                                    showHtml.find('#targetRouteFilterListR_R').show();
                                    if (dataMap.targetFilter) {
                                        var targetFilters = dataMap.targetFilter;
                                        var selectOptionHtml = '<option value="">Please click Select Filter Country</option>';
                                        for (var i = 0; i < targetFilters.length; i++) {
                                            var targetFilter = targetFilters[i];
                                            selectOptionHtml += '<option value="' + targetFilter.name + '" title="' + targetFilter.customValue + '">' + targetFilter.name + '</option>';
                                        }
                                        showHtml.find('#targetRouteFilterSelectR_R').html(selectOptionHtml);
                                    } else {
                                        showHtml.find('#targetRouteFilterSelectR_R').parent().html("Route no filter rule");
                                    }
                                }
                            }
                            showHtml.find('#targetTypeDivR_R').html(targetTypeStr);
                            showHtml.find('#targetTitleStrR_R').html('Target:' + dataMap.targetName);
                            if ("Default" === sourceTypeStr && "Default" === targetTypeStr) {
                            } else if ("None" === sourceTypeStr || "None" === targetTypeStr) {
                            } else {
                                layuiOpenWindowDivFunc('SET PATN PROT WINDOWS', showHtml.html());
                            }
                        } else {
                            graphGlobal.removeCells(pathsCells);
                        }
                    } else {
                        graphGlobal.removeCells(pathsCells);
                    }
                }
            });

        }
    }
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

//Erase drawing board records
function eraseRecord() {
    thisEditor.lastSnapshot = new Date().getTime();
    thisEditor.undoManager.clear();
    thisEditor.ignoredChanges = 0;
    thisEditor.setModified(false);
}

function openAddStopCustomAttrPage(stopId) {
    var addStopCustomizedAttrOpenTemplate = $("#addStopCustomizedAttrOpenTemplate").clone();
    addStopCustomizedAttrOpenTemplate.find("form").attr("id", "openAddStopCustomAttrId");
    addStopCustomizedAttrOpenTemplate.find("#openAddCustomizedWindowStopId").hide();
    addStopCustomizedAttrOpenTemplate.find("#openAddCustomizedWindowStopId").attr("value", stopId);
    layer.open({
        type: 1,
        title: "Add Customized Property",
        shadeClose: true,
        closeBtn: 1,
        shift: 7,
        anim: 5,//Bounce up and down
        shade: 0.1,
        // resize: false,//No stretch
        // move: false,//No drag and drop
        // offset: ['' + p.top + 'px', '' + p.left + 'px'],//coordinate
        area: ['555px', '340px'], //Width height
        content: addStopCustomizedAttrOpenTemplate.html()
    });
}


function addStopCustomProperty(reqData) {
    ajaxRequest({
        type: "POST",//Request type post
        url: "/stops/addStopCustomizedProperty",//This is the name of the file where I receive data in the background.
        data: reqData,
        error: function (request) {//Operation after request failure
            return;
        },
        success: function (data) {//Operation after request successful
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                layer.msg("add success", {icon: 1, shade: 0, time: 1000}, function () {
                    layer.closeAll();
                    queryStopsProperty(dataMap.stopPageId);
                });
            } else {
                layer.msg(dataMap.errorMsg, {icon: 2, shade: 0, time: 2000}, function () {
                });
            }
        }
    });

}

function removeStopCustomProperty(stopPageId, customPropertyId, isRouter) {
    if (isRouter) {
        getRouterAllPaths(customPropertyId)
    } else {
        ajaxRequest({
            type: "POST",//Request type post
            url: "/stops/deleteStopsCustomizedProperty",//This is the name of the file where I receive data in the background.
            data: {customPropertyId: customPropertyId},
            error: function (request) {//Operation after request failure
                return;
            },
            success: function (data) {//Operation after request successful
                var dataMap = JSON.parse(data);
                //console.log(dataMap);
                if (200 === dataMap.code) {
                    layer.msg("add success", {icon: 1, shade: 0, time: 1000}, function () {
                        layer.closeAll();
                        queryStopsProperty(stopPageId);
                    });
                } else {
                    layer.msg(dataMap.errorMsg, {icon: 2, shade: 0, time: 2000}, function () {
                    });
                }
            }
        });
    }
}

function getRouterAllPaths(customPropertyId) {
    ajaxRequest({
        type: "POST",//Request type post
        url: "/stops/getRouterStopsCustomizedProperty",//This is the name of the file where I receive data in the background.
        data: {customPropertyId: customPropertyId},
        error: function (request) {//Operation after request failure
            return;
        },
        success: function (data) {//Operation after request successful
            var dataMap = JSON.parse(data);
            //console.log(dataMap);
            if (200 === dataMap.code) {
                if (dataMap.pathsVoList) {
                    var showPathsHtml = '<span>Deleting this rule will affect the following:</span>';
                    layer.confirm(showPathsHtml, {icon: 1, shade: 0, time: 1000}, function () {
                    });
                } else {
                    //removeRouterStopCustomProperty(customPropertyId);
                    console.log("failed");
                }
            } else {
                layer.msg(dataMap.errorMsg, {icon: 2, shade: 0, time: 2000}, function () {
                });
            }
        }
    });
}

function removeRouterStopCustomProperty(customPropertyId) {
    ajaxRequest({
        type: "POST",//Request type post
        url: "/stops/deleteRouterStopsCustomizedProperty",//This is the name of the file where I receive data in the background.
        data: {customPropertyId: customPropertyId},
        error: function (request) {//Operation after request failure
            return;
        },
        success: function (data) {//Operation after request successful
            var dataMap = JSON.parse(data);
            //console.log(dataMap);
            if (200 === dataMap.code) {
                layer.msg("add success", {icon: 1, shade: 0, time: 1000}, function () {
                    layer.closeAll();
                    queryStopsProperty(stopPageId);
                });
            } else {
                layer.msg(dataMap.errorMsg, {icon: 2, shade: 0, time: 2000}, function () {
                });
            }
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

function openDatasourceList() {
    var window_width = $(window).width();//Get browser window width
    var window_height = $(window).height();//Get browser window height
    ajaxRequest({
        type: "POST",//Request type post
        url: "/page/datasource/getDatasourceListPage",
        error: function (request) {//Operation after request failure
            return;
        },
        success: function (data) {//Operation after request successful
            layer.open({
                type: 1,
                title: '<span style="color: #269252;">DatasourceList</span>',
                shadeClose: false,
                closeBtn: 1,
                shift: 7,
                area: [(window_width - 100) + 'px', (window_height - 100) + 'px'], //Width height
                skin: 'layui-layer-rim', //Add borders
                content: data
            });
        }
    });
}


function deleteLastReloadData(stopId) {
    ajaxRequest({
        type: "POST",//Request type post
        url: "/stops/deleteLastReloadData",//This is the name of the file where I receive data in the background.
        data: {stopId: stopId},
        error: function (request) {//Operation after request failure
            return;
        },
        success: function (data) {//Operation after request successful
            var dataMap = JSON.parse(data);
            if (200 == dataMap.code) {
                layer.msg(dataMap.errorMsg, {
                    icon: 1,
                    shade: 0,
                    time: 2000
                }, function () {
                    $("#del_last_reload_div").hide();
                    $("#del_last_reload_table").hide();
                });
            } else {
                layer.msg(dataMap.errorMsg, {
                    icon: 2,
                    shade: 0,
                    time: 2000
                }, function () {

                });
            }
        }
    });
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
                if (dataMap.groupsVoList) {
                    if (dataMap.groupsVoList && dataMap.groupsVoList.length > 0) {
                        for (var i = 0; i < dataMap.groupsVoList.length; i++) {
                            var groupsVoList_i = dataMap.groupsVoList[i];
                            if (groupsVoList_i && '' !== groupsVoList_i) {
                                Sidebar.prototype.component_data.push({
                                    component_name: groupsVoList_i.groupName,
                                    component_group: groupsVoList_i.stopsTemplateVoList,
                                    component_prefix: (web_header_prefix + "/images/"),
                                    addImagePaletteId: 'clipart'
                                });
                            }
                        }
                    }
                }
            } else {
                //window.location.href = (web_header_prefix + "/error/404");
            }
            $('#fullScreen').hide();
        },
        error: function (request) {//Operation after request failure
            //window.location.href = (web_header_prefix + "/error/404");
            return;
        }
    });
}

function initGraph() {
    if (null != flowGroupData && flowGroupData.length > 0) {
        for (var i = 0; i < flowGroupData.length; i++) {
            var flowGroupData_i = flowGroupData[i];
            if (flowGroupData_i && '' !== flowGroupData_i) {
                Sidebar.prototype.component_data.push({
                    component_name: flowGroupData_i.groupName,
                    component_group: flowGroupData_i.dataList,
                    component_prefix: "/piflow-web/img/",
                    addImagePaletteId: "general"
                });
            }
        }
    }
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
            if (isExample) {
                prohibitEditing(evt, 'ADD');
            } else {
                addMxCellOperation(evt);
            }
        });
        graphGlobal.addListener(mxEvent.CELLS_MOVED, function (sender, evt) {
            if (isExample) {
                prohibitEditing(evt, 'MOVED');
            } else {
                movedMxCellOperation(evt);
            }
        });
        graphGlobal.addListener(mxEvent.CELLS_REMOVED, function (sender, evt) {

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
        graphGlobal.addListener(mxEvent.DOUBLE_CLICK, function (sender, evt) {
            OpenTheMonitorArtboard(evt);

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

function movedMxCellOperation(evt) {
    statusgroup = ""
    if (evt.properties.disconnect) {
        saveXml(null, 'MOVED');   // preservation method
    }
    //findBasicInfo(evt);
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
    $('#flowGroup_property_inc_loading').show();
    $('#flowGroup_property_inc_load_fail').hide();
    $('#flowGroup_property_inc_no_data').hide();
    $('#flowGroup_property_inc_load_data').hide();
    ajaxRequest({
        cache: true,
        type: "POST",
        url: "/flowGroup/queryIdInfo",
        data: {"fId": loadId, "pageId": pageId},
        async: true,
        error: function (request) {
            $('#flowGroup_property_inc_loading').hide();
            $('#flowGroup_property_inc_load_fail').show();
            return;
        },
        success: function (data) {
            $('#flowGroup_property_inc_loading').hide();
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                var nodeType = dataMap.nodeType;
                if ("flowGroup" === nodeType) {
                    var flowGroupVo = dataMap.flowGroupVo;
                    $("#div_flowGroupVo_basicInfo_id").show();
                    $("#div_flowGroupVo_attributeInfo_id").show();
                    $("#div_flowVo_basicInfo_id").hide();
                    $("#div_flowVo_attributeInfo_id").hide();

                    console.log(flowGroupVo);
                    $('#flowGroup_property_inc_load_data').show();
                } else if ("flow" === nodeType) {
                    var flowVo = dataMap.flowVo;
                    $("#div_flowGroupVo_basicInfo_id").hide();
                    $("#div_flowGroupVo_attributeInfo_id").hide();
                    $("#div_flowVo_basicInfo_id").show();
                    $("#div_flowVo_attributeInfo_id").show();

                    // ----------------------- baseInfo start -----------------------
                    $('#input_flowVo_id').val(flowVo.id);
                    $('#input_flowVo_pageId').val(flowVo.pageId);
                    $('#span_flowVo_name').text(flowVo.name);
                    $('#span_flowVo_description').text(flowVo.description);
                    $('#span_flowVo_driverMemory').text(flowVo.driverMemory);
                    $('#span_flowVo_executorCores').text(flowVo.executorCores);
                    $('#span_flowVo_executorMemory').text(flowVo.executorMemory);
                    $('#span_flowVo_executorNumber').text(flowVo.executorNumber);
                    $('#span_flowVo_crtDttmString').text(flowVo.crtDttmString);
                    $('#span_flowVo_stopQuantity').text(flowVo.stopQuantity);
                    if (isExample) {
                        $('#btn_show_update').hide();
                    }
                    // ----------------------- baseInfo end   -----------------------

                    // ----------------------- AttributeInfo start -----------------------
                    $('#input_flowVo_description').val(flowVo.description);
                    $('#input_flowVo_description').attr("name", "description");
                    $('#input_flowVo_description').attr("onclick", "openUpdateCellsProperty(this ,'flow')");
                    $('#input_flowVo_driverMemory').val(flowVo.driverMemory);
                    $('#input_flowVo_driverMemory').attr("name", "driverMemory");
                    $('#input_flowVo_driverMemory').attr("onclick", "openUpdateCellsProperty(this ,'flow')");
                    $('#input_flowVo_executorCores').val(flowVo.executorCores);
                    $('#input_flowVo_executorCores').attr("name", "executorCores");
                    $('#input_flowVo_executorCores').attr("onclick", "openUpdateCellsProperty(this ,'flow')");
                    $('#input_flowVo_executorMemory').val(flowVo.executorMemory);
                    $('#input_flowVo_executorMemory').attr("name", "executorMemory");
                    $('#input_flowVo_executorMemory').attr("onclick", "openUpdateCellsProperty(this ,'flow')");
                    $('#input_flowVo_executorNumber').val(flowVo.executorNumber);
                    $('#input_flowVo_executorNumber').attr("name", "executorNumber");
                    $('#input_flowVo_executorNumber').attr("onclick", "openUpdateCellsProperty(this ,'flow')");
                    // ----------------------- AttributeInfo end   -----------------------
                    $('#flowGroup_property_inc_load_data').show();
                } else {
                    $('#flowGroup_property_inc_no_data').show();
                }
            } else {
                $('#flowGroup_property_inc_load_fail').show();
            }
            layer.close(layer.index);
        }
    });
}

function prohibitEditing(evt, operationType) {
    if ('ADD' === operationType || 'REMOVED' === operationType) {
        layer.msg("This is an example, you can't add, edit or delete", {
            icon: 2,
            shade: 0,
            time: 2000
        }, function () {

        });
    } else if ('MOVED' === operationType) {
        findBasicInfo(evt);
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
            eraseRecord()
            return;
        },
        success: function (data) {//After the request is successful
            if ('ADD' === operationType || 'REMOVED' === operationType) {
                location.reload();
            }
            eraseRecord()
        }
    });
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
                    $("#description1").val("");
                    layer.open({
                        type: 1,
                        title: '<span style="color: #269252;">create flow group</span>',
                        shadeClose: false,
                        shade: 0.3,
                        closeBtn: 1,
                        shift: 7,
                        area: ['580px', '520px'], //Width height
                        skin: 'layui-layer-rim', //Add borders
                        content: $("#SubmitPage"),
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
                                            // layer.closeAll()
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
                        content: $("#SubmitPageFlow"),
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
                                            // layer.closeAll()
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

                } else if (statusgroup == null || statusgroup == "" || 'TASK' === Format.customizeType) {


                } else {

                    if (graphGlobal.isEnabled()) {
                        graphGlobal.startEditingAtCell();
                    }
                }
                thisEditor.setModified(false);
                if (operType && '' !== operType) {
                    //获取port
                    //getStopsPort(paths);
                    if ('TASK' === Format.customizeType) {
                        getStopsPortNew(paths);
                    }
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

//update stops name button
function isShowUpdateCellsName(flag) {
    if (flag) {
        $("#input_flowVo_name").val($("#span_flowVo_name").text());
        $("#tr_flowVo_name_info").hide();
        $("#tr_flowVo_name_input").show();
    } else {
        $("#tr_flowVo_name_info").show();
        $("#tr_flowVo_name_input").hide();
    }
}

//update stops property
function openUpdateCellsProperty(e, cellType) {
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

    if ("flowGroup" === cellType) {
        var flowId = $("#input_flowGroupVo_id").val();
        updateCellsPropertyTemplateClone.find("#cellsPropertyValueBtn").attr("onclick", ("updateFlowGroupAttributes('" + flowId + "','" + e.id + "','cellsPropertyValue',this);"));
    } else {
        var flowGroupId = $("#input_flowVo_id").val();
        updateCellsPropertyTemplateClone.find("#cellsPropertyValueBtn").attr("onclick", ("updateFlowAttributes('" + flowGroupId + "','" + e.id + "','cellsPropertyValue',this)"));
    }
    console.log(e);
    var p = $(e).offset();
    var openWindowCoordinate = [(p.top + 34) + 'px', (document.body.clientWidth - 300) + 'px'];
    console.log(openWindowCoordinate);
    layer.open({
        type: 1,
        title: name,
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
    var driverMemoryObj = $("#id0");
    var executorCoresObj = $("#id1");
    var executorMemoryObj = $("#id2");
    var executorNumberObj = $("#id3");
    var descriptionObj = $("#id4");


    ajaxRequest({
        cache: true,
        type: "POST",
        url: "/flow/updateFlowBaseInfo",
        data: {
            "id": flowId,
            "description": $('#input_flowVo_description').val(),
            "driverMemory": $('#input_flowVo_driverMemory').val(),
            "executorCores": $('#input_flowVo_executorCores').val(),
            "executorMemory": $('#input_flowVo_executorMemory').val(),
            "executorNumber": $('#input_flowVo_executorNumber').val()

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
                $('#input_flowVo_description').val(flowVo.description)
                $('#input_flowVo_driverMemory').val(flowVo.driverMemory);
                $('#input_flowVo_executorCores').val(flowVo.executorCores);
                $('#input_flowVo_executorMemory').val(flowVo.executorMemory);
                $('#input_flowVo_executorNumber').val(flowVo.executorNumber);
                //baseInfo
                $('#span_flowVo_description').text(flowVo.description);
                $('#span_flowVo_driverMemory').text(flowVo.driverMemory);
                $('#span_flowVo_executorCores').text(flowVo.executorCores);
                $('#span_flowVo_executorMemory').text(flowVo.executorMemory);
                $('#span_flowVo_executorNumber').text(flowVo.executorNumber);
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
            "id": id,
            "description": $("#input_flowGroupVo_description").val()
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
                $("#input_flowGroupVo_description").val(flowGroupVo.description)
                //baseInfo
                $("#span_flowGroupVo_description").text(flowGroupVo.description);
            } else {
                layer.msg('', {icon: 2, shade: 0, time: 2000}, function () {
                });
            }
            layer.closeAll('page');
            console.log("attribute update success");
        }
    });
}

