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
            //console.log("aaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            //console.log(evt);
        });
        graphGlobal.addListener(mxEvent.CELLS_MOVED, function (sender, evt) {
            processListener(evt, "MOVED");
            //console.log("uuuuuuuuuuuuuuu");
            //console.log(evt);

        });
        graphGlobal.addListener(mxEvent.CELLS_REMOVED, function (sender, evt) {
            processListener(evt, "REMOVED");
        });
        graphGlobal.addListener(mxEvent.CLICK, function (sender, evt) {
            findBasicInfo(evt);
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
            queryStopsProperty(maxStopPageId);
        }
    }
    if (typeof (cells) != "undefined" && null != id && "" != id && "null" != id) {
        if (document.getElementById("stopsNameLabel"))
            document.getElementById('stopsNameLabel').value = value;
        //Check the case of Path
        if (cells.cell && cells.cell.edge) {
            if (cells.cell.target && cells.cell.source) {
                //Query Path
                queryPathInfo(id);
            }
        } else {
            //Query Stops and attribute information；
            queryStopsProperty(id);
        }
    }
}

function queryStopsProperty(stopPageId) {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/piflow-web/stops/queryIdInfo",
        data: {"stopPageId": stopPageId, "fid": loadId},
        async: true,
        error: function (request) {
            //alert("Jquery Ajax request error!!!");
            return;
        },
        success: function (data) {
            if ("" != data) {
                var addParamData = {
                    data: data.propertiesVo,
                    stopId: data.id,
                    isCheckpoint: data.checkpoint,
                    stopPageId: stopPageId,
                    isCustomized: data.isCustomized,
                    stopsCustomizedPropertyVoList: data.stopsCustomizedPropertyVoList,
                    stopOutPortType: data.outPortType,
                    dataSourceVo: data.dataSourceVo
                };
                //add(data.propertiesVo, data.id, data.checkpoint, stopPageId, data.isCustomized, data.stopsCustomizedPropertyVoList, data.outPortType, data.dataSourceVo);
                add(addParamData);
                //  $("#stopsValueInput").data("result",evt);
                $('#stopsNameSpan').text(data.name);
                $('#stopsNameLabel').attr("value", data.name);
                $('#stopsNameLabel').attr("name", data.id);
                $('#stopsValueInput').attr("value", data.name);
                $('#stopsValueInput').attr("name", data.pageId);
                $('#stopsDescription').text(data.description);
                $('#stopsGroups').text(data.groups);
                $('#stopsBundel').text(data.bundel);
                $('#stopsVersion').text(data.version);
                $('#stopSowner').text(data.owner);
                $('#stopCreateDate').text(data.crtDttmString);

                //document.getElementById('stopsNameSpan').innerText = data.name;
                //document.getElementById('stopsNameLabel').value = data.name;
                //document.getElementById('stopsNameLabel').name = data.id;
                //document.getElementById('stopsValueInput').value = data.name;
                //document.getElementById('stopsValueInput').name = data.pageId;
                //document.getElementById('stopsDescription').innerText = data.description;
                //document.getElementById('stopsGroups').innerText = data.groups;
                //document.getElementById('stopsBundel').innerText = data.bundel;
                //document.getElementById('stopsVersion').innerText = data.version;
                //document.getElementById('stopSowner').innerText = data.owner;
                //document.getElementById('stopCreateDate').innerText = data.crtDttmString;

                /* document.getElementById('stopStateId').innerText = data.state ? data.state : "No state";
                 document.getElementById('stopsStartTimeId').innerText = data.startTimes ? data.startTimes : "No startTime";
                 document.getElementById('stopEndTimeId').innerText = data.stopTimes ? data.stopTimes : "No stopTimes";*/
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

function queryPathInfo(id) {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/piflow-web/path/queryPathInfo",
        data: {"id": id, "fid": loadId},
        async: true,
        error: function (request) {
            //alert("Jquery Ajax request error!!!");
            return;
        },
        success: function (data) {
            var dataMap = JSON.parse(data);
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
                    $("#descriptionID").html('flowName：');
                    $("#stopsNameID").html('pageId：');
                    $("#GroupNameID").html('inport：');
                    $("#bundelID").html('outport：');
                    $("#versionID").html('form：');
                    $("#ownerID").html('to：');
                    $("#createDateID").html('createTime：');
                    $("#updateStopNameBtn").hide();
                    document.getElementById('stopsDescription').innerText = queryInfo.flow.name;
                    document.getElementById('stopsNameLabel').value = queryInfo.pageId;
                    document.getElementById('stopsGroups').innerText = queryInfo.inport;
                    document.getElementById('stopsBundel').innerText = queryInfo.outport;
                    document.getElementById('stopsVersion').innerText = queryInfo.stopFrom.name;
                    document.getElementById('stopSowner').innerText = queryInfo.stopTo.name;
                    document.getElementById('stopCreateDate').innerText = queryInfo.crtDttmString;
                    //document.getElementById('table_idDiv').style.display = 'none';
                }
            } else {
                console.log("Path attribute query null");
            }
        }
    });
}

function add(addParamData) {
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
            displayName.setAttribute('onclick', 'stopTabTd(this,false)');
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
            $.ajax({
                cache: true,
                type: "POST",
                url: "/piflow-web/stops/updateStops",
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
            + '<td id="datasourceTdElement">'
            // + '<select id="datasourceSelectElement" onblur="alert(1);" class="form-control" style="height: 32px;">'
            + '<select id="datasourceSelectElement" class="form-control" style="height: 32px;">'
            + '<option value="" selected="selected"></option>'
            + '<option value="111" style="background-color: rgb(219, 219, 219);">111</option>'
            + '<option value="222" style="background-color: rgb(219, 219, 219);">111</option>'
            + '</select>'
            + '</td>'
            + '<td><button class="btn" onclick="openDatasourceList()" style="margin-right: 2px;"><i class="glyphicon glyphicon-edit"></i></button></td>'
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
        /*
        $(divValue).append('<div id="fill_datasource_id" style="text-align: right;margin-right: 10px;">'
            + '<input type="button" style="background: #1A8B5F;" class="btn btn-success" '
            + 'onclick="getDatasourceList(\'' + addParamData.stopId + '\',\'' + addParamData.stopPageId + '\')" '
            + 'value="Fill Datasource"/></div>');
        */

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
            + 'onclick="stopTabTd(this,true)"readonly="readonly" value=""style="background: rgb(245, 245, 245);">'
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
    $.ajax({
        cache: true,//sava cache data
        type: "POST",// request type
        url: "/piflow-web/datasource/getDatasourceList",

        //data:$('#loginForm').serialize(),//Form serialization
        async: true,//open asynchronous request
        error: function (request) {//Operation after request failure
            return;
        },
        success: function (data) {//Operation after request successful
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                /*
                var html = '<div class="layui-form-item" style="margin-top: 22px;">'
                    + '<label class="layui-form-label">type</label>'
                    + '<div class="layui-input-block">'
                    + '<select class="layui-select" style="width: 95%;" onchange="fillDatasource(this,\'' + stop_id + '\',\'' + stop_page_id + '\')">'
                    + '<option value="">please select type...</option>';
                var dataSourceList = dataMap.data;
                if (dataSourceList && dataSourceList.length > 0) {
                    for (var i = 0; i < dataSourceList.length; i++) {
                        html += ('<option value="' + dataSourceList[i].id + '">'
                            + dataSourceList[i].dataSourceName + '(' + dataSourceList[i].dataSourceType + ')'
                            + '</option>');
                    }
                }
                html += ('</select></div></div>');
                layer.open({
                    type: 1,
                    title: '<span style="color: #269252;">data source</span>',
                    shadeClose: true,
                    closeBtn: 1,
                    resize: false,
                    shift: 7,
                    area: ['580px', '150px'], //Width height
                    skin: 'layui-layer-rim', //Add borders
                    content: html
                });
                */
                var dataSourceList = dataMap.data;
                var select_html = '<select id="datasourceSelectElement" class="form-control" style="width: 95%;" onchange="fillDatasource(this,\'' + stop_id + '\',\'' + stop_page_id + '\')">'
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
                select_html += (option_html + "</select>");
                $('#datasourceTdElement').html(select_html);
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
        $.ajax({
            cache: true,//Keep cached data
            type: "POST",//Request type post
            url: "/piflow-web/datasource/fillDatasource",//This is the name of the file where I receive data in the background.
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

// Adding Operational Processing
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

//Save XML files and related information
function saveXml(paths, operType) {
    var getXml = thisEditor.getGraphXml();
    var xml_outer_html = getXml.outerHTML;
    //var waitxml = encodeURIComponent(getXml.outerHTML);//This is the XML code to submit to the background

    $.ajax({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/piflow-web/grapheditor/saveData",//This is the name of the file where I receive data in the background.
        //data:$('#loginForm').serialize(),//Serialize the form
        data: {
            imageXML: xml_outer_html,
            load: loadId,
            operType: operType
        },
        async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            return;
        },
        success: function (data) {//Operation after request successful
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                console.log(operType + " save success");
                thisEditor.setModified(false);
                if (operType && '' !== operType) {
                    //获取port
                    //getStopsPort(paths);
                    getStopsPortNew(paths);
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
        type: "POST",//Request type post
        url: "/piflow-web/flow/loadData",//This is the name of the file where I receive data in the background.
        //data:$('#loginForm').serialize(),//Serialize the form
        async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            return;
        },
        success: function (data) {//Operation after request successful
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
    $.ajax({
        data: {"load": loadId},
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/piflow-web/grapheditor/reloadStops",//This is the name of the file where I receive data in the background.
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
                window.location.href = "/piflow-web/grapheditor/home?load=" + dataMap.load + "&_" + new Date().getTime();
            } else {
                //alert("reload fail");
                layer.msg("reload fail", {icon: 2, shade: 0, time: 2000}, function () {
                });
                fullScreen.hide();
            }
        }
    });
}

function queryFlowInfo() {
    $.ajax({
        data: {"load": loadId},
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/piflow-web/flow/queryFlowData",//This is the name of the file where I receive data in the background.
        async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            return;
        },
        success: function (data) {//Operation after request successful
            var dataMap = JSON.parse(data);
            if (document.getElementById("UUID")) {//JS judges whether an element exists or not
                var flow = dataMap.flow;
                if (flow != null && flow != "")
                    var flowInfoDbInfo = flow.flowInfoDbVo;
                if (flow != null && flow != "") {
                    document.getElementById('UUID').innerText = flow.uuid ? flow.uuid : "No content";
                    document.getElementById('flowName').innerText = flow.name ? flow.name : "No content";
                    document.getElementById('flowDescription').innerText = flow.description ? flow.description : "No content";
                    document.getElementById('createTime').innerText = flow.crtDttmString ? flow.crtDttmString : "No content";
                } else {
                    document.getElementById('UUID').innerText = "No content";
                    document.getElementById('flowName').innerText = "No content";
                    document.getElementById('flowDescription').innerText = "No content";
                    document.getElementById('createTime').innerText = "No content";
                }
                getRunningProcessList();
            }
        }
    });
}

//run
function runFlow(runMode) {
    fullScreen.show();
    console.info("ss");
    var data = {flowId: loadId}
    if (runMode) {
        data.runMode = runMode;
    }
    $.ajax({
        cache: true,//Keep cached data
        type: "POST",//Request type post
        url: "/piflow-web/flow/runFlow",//This is the name of the file where I receive data in the background.
        //data:$('#loginForm').serialize(),//Serialize the form
        data: data,
        async: true,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            //alert("Request Failed");
            layer.msg("Request Failed", {icon: 2, shade: 0, time: 2000}, function () {
                fullScreen.hide();
            });

            return;
        },
        success: function (data) {//Operation after request successful
            //console.log("success");
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                layer.msg(dataMap.errorMsg, {icon: 1, shade: 0, time: 2000}, function () {
                    //Jump to the monitor page after starting successfully
                    var tempWindow = window.open('_blank');
                    if (tempWindow == null || typeof (tempWindow) == 'undefined') {
                        alert('The window cannot be opened. Please check your browser settings.')
                    } else {
                        tempWindow.location = "/piflow-web/process/getProcessById?parentAccessPath=grapheditor&processId=" + dataMap.processId;
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
            $.ajax({
                cache: true,
                type: "get",
                url: "/piflow-web/grapheditor/getStopsPort",
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
                    //console.log(dataMap);
                    if (200 === dataMap.code) {
                        var showHtml = $('#portShowDiv').clone();
                        showHtml.find('#protInfo1Copy').attr('id', 'protInfoR_R');
                        showHtml.find('#sourceTitle1Copy').attr('id', 'sourceTitleR_R');
                        showHtml.find('#sourceTitleStr1Copy').attr('id', 'sourceTitleStrR_R');
                        showHtml.find('#sourceTitleCheckbox1Copy').attr('id', 'sourceTitleCheckboxR_R');
                        showHtml.find('#sourceTitleBtn1Copy').attr('id', 'sourceTitleBtnR_R');
                        showHtml.find('#sourceCrtPortId1Copy').attr('id', 'sourceCrtPortIdR_R');
                        showHtml.find('#sourceCrtPortBtnId1Copy').attr('id', 'sourceCrtPortBtnIdR_R');
                        showHtml.find('#sourceCrtPortBtnIdR_R').attr('onclick', 'crtAnyPort("sourceCrtPortIdR_R",true)');
                        showHtml.find('#sourceTypeDiv1Copy').attr('id', 'sourceTypeDivR_R');
                        showHtml.find('#sourceRouteFilterList1Copy').attr('id', 'sourceRouteFilterListR_R');
                        showHtml.find('#sourceRouteFilterSelect1Copy').attr('id', 'sourceRouteFilterSelectR_R');
                        showHtml.find('#sourceTitleBtnR_R').hide();
                        showHtml.find('#sourceRouteFilterListR_R').hide();
                        showHtml.find('#targetTitle1Copy').attr('id', 'targetTitleR_R');
                        showHtml.find('#targetTitleStr1Copy').attr('id', 'targetTitleStrR_R');
                        showHtml.find('#targetTitleCheckbox1Copy').attr('id', 'targetTitleCheckboxR_R');
                        showHtml.find('#targetTitleBtn1Copy').attr('id', 'targetTitleBtnR_R');
                        showHtml.find('#targetCrtPortId1Copy').attr('id', 'targetCrtPortIdR_R');
                        showHtml.find('#targetCrtPortBtnId1Copy').attr('id', 'targetCrtPortBtnIdR_R');
                        showHtml.find('#targetCrtPortBtnIdR_R').attr('onclick', 'crtAnyPort("targetCrtPortIdR_R",true)');
                        showHtml.find('#targetTypeDiv1Copy').attr('id', 'targetTypeDivR_R');
                        showHtml.find('#targetRouteFilterList1Copy').attr('id', 'targetRouteFilterListR_R');
                        showHtml.find('#targetRouteFilterSelect1Copy').attr('id', 'targetRouteFilterSelectR_R');
                        showHtml.find('#targetTitleBtnR_R').hide();
                        showHtml.find('#targetRouteFilterListR_R').hide();
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
            $.ajax({
                cache: true,
                type: "get",
                url: "/piflow-web/grapheditor/savePathsPort",
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
                $('#sourceTitleCheckboxR_R').append(obj);
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
    $.ajax({
        cache: true,
        type: "POST",
        url: "/piflow-web/stops/updateStopsById",
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

function saveTemplate() {
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
            type: "POST",//Request type post
            url: "/piflow-web/template/saveTemplate",//This is the name of the file where I receive data in the background.
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
            success: function (data) {//Operation after successful request
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

function uploadXmlFile() {
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
    var obj = document.getElementById('uploadFile');
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
    $.ajax({
        type: 'post',
        data: {
            templateId: id,
            load: loadId
        },
        async: true,
        url: "/piflow-web/template/loadingXmlPage",
    }).success(function (data) {
        window.location.reload();
    }).error(function () {
    });
}

function showSelect() {
    var bt = document.getElementById("loadingXml");
    bt.onclick = function () {
        var oDiv = document.getElementById("divloadingXml");
        if (oDiv.style.display == "block") {
            oDiv.style.display = "none";
        } else {
            if (isExample) {
                layer.msg('This is an example, you can\'t edit', {icon: 2, shade: 0, time: 2000}, function () {
                });
                return;
            }
            loadingSelect();
            oDiv.style.display = "block";
        }
    }
}

function loadingSelect() {
    $("#loadingXmlSelect").html("");
    $.ajax({
        url: "/piflow-web/template/templateAllSelect",
        type: "post",
        async: false,
        success: function (data) {
            var dataMap = JSON.parse(data);
            if (200 === dataMap.code) {
                var temPlateList = dataMap.temPlateList;
                $("#loadingXmlSelect").append("<option value='-1' >------------please choose------------</option>");
                for (var i = 0; i < temPlateList.length; i++) {
                    $("#loadingXmlSelect").append("<option value=" + temPlateList[i].id + " >" + temPlateList[i].name + "</option>");
                }
            }
        }
    });
}

function openTemplateList() {
    if (isExample) {
        layer.msg('This is an example, you can\'t edit', {icon: 2, shade: 0, time: 2000}, function () {
        });
        return;
    }
    $.ajax({
        url: "/piflow-web/template/templateAllSelect",
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
                        + '<input type="button" class="btn" value="Submit" onclick="loadTemplate()"/>'
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
            }
        }
    });
}

function loadTemplate() {
    var id = $("#loadingXmlSelectNew").val();
    if (id == '-1') {
        layer.msg('Please choose template', {icon: 2, shade: 0, time: 2000}, function () {
        });
        return;
    }

    var name = $("#loadingXmlSelectNew option:selected").text();
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
        type: "POST",//Request type post
        url: "/piflow-web/exampleMenu/exampleUrlList",//This is the name of the file where I receive data in the background.
        data: {},
        async: true,
        error: function (request) {//Operation after request failure
            if ('ADD' === operType || 'REMOVED' === operType) {
                location.reload();
            }
            eraseRecord()
            return;
        },
        success: function (data) {//Operation after request successful
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
        type: "POST",//Request type post
        url: "/piflow-web/grapheditor/getRunningProcessList",//This is the name of the file where I receive data in the background.
        data: {"flowId": loadId},
        async: true,
        error: function (request) {//Operation after request failure
            return;
        },
        success: function (data) {//Operation after request successful
            $('#runningProcessID').remove();
            $('#rightSidebarID').append(data);
        }
    });
}

//Erase Sketchpad records
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
    $.ajax({
        type: "POST",//Request type post
        url: "/piflow-web/stops/addStopCustomizedProperty",//This is the name of the file where I receive data in the background.
        data: reqData,
        error: function (request) {//Operation after request failure
            return;
        },
        success: function (data) {//Operation after request successful
            var dataMap = JSON.parse(data);
            //console.log(dataMap);
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
        var reqUrl = "/piflow-web/stops/deleteStopsCustomizedProperty";
        var reqData = {customPropertyId: customPropertyId};
        $.ajax({
            type: "POST",//Request type post
            url: reqUrl,//This is the name of the file where I receive data in the background.
            data: reqData,
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
    var reqUrl = "/piflow-web/stops/getRouterStopsCustomizedProperty";
    var reqData = {customPropertyId: customPropertyId};
    $.ajax({
        type: "POST",//Request type post
        url: reqUrl,//This is the name of the file where I receive data in the background.
        data: reqData,
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
                        console.log("sssssss");
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
    var reqUrl = "/piflow-web/stops/deleteRouterStopsCustomizedProperty";
    var reqData = {customPropertyId: customPropertyId};
    $.ajax({
        type: "POST",//Request type post
        url: reqUrl,//This is the name of the file where I receive data in the background.
        data: reqData,
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
    $.ajax({
        type: "POST",//Request type post
        url: "/piflow-web/datasource/getDatasourceListPage",//This is the name of the file where I receive data in the background.
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