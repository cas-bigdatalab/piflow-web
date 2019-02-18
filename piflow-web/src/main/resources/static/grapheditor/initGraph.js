// Extends EditorUi to update I/O action states based on availability of backend
var graphGlobal = null;
var thisEditor = null;
var sign = true;
var fullScreen = $('#fullScreen');
var pathsCells = [];
var flag = 0;
var timerPath;

function initGraph() {
    var editorUiInit = EditorUi.prototype.init;

    EditorUi.prototype.init = function () {
        editorUiInit.apply(this, arguments);
        graphGlobal = this.editor.graph;
        thisEditor = this.editor;

        //监控事件
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
    EditorUi.prototype.menubarHeight = 0;
    EditorUi.prototype.menubarShow = false;
}

function findBasicInfo(evt) {
    flag = 0;
    var id = null;
    var value = null;
    var ss = [];
    //针对不同事件设置值
    var cells = evt.properties.cells;
    if (null != cells) {
        //cellsAdded和cellsMoved操作走这里
        value = cells[0].value;
        id = cells[0].id;
    } else {
        cells = evt.properties;
        if (null != cells.cell) {
            //点击stop操作
            value = cells.cell.value;
            id = cells.cell.id;
        } else {
            //第一次给画板添加stop时，拖拽过程获取不到id时
            queryStopsProperty(maxStopId);
        }
    }
    if (typeof (cells) != "undefined" && null != id && "" != id && "null" != id) {
        if (document.getElementById("stopsNameLabel"))
            document.getElementById('stopsNameLabel').value = value;
        //选中是线的情况
        if (cells.cell && cells.cell.edge) {
            if (cells.cell.target && cells.cell.source) {
                //查询phth
                queryPathInfo(id);
            }
        } else {
            //查询stops以及属性信息；
            queryStopsProperty(id);
        }
    }
}

function queryStopsProperty(id) {
    $.ajax({
        cache: true,
        type: "POST",
        url: "/piflow-web/stops/queryIdInfo",
        data: {"id": id, "fid": loadId},
        async: true,
        error: function (request) {
            //alert("Jquery Ajax request error!!!");
            return;
        },
        success: function (data) {
            if ("" != data) {
                add(data.propertiesVo, data.id, data.checkpoint);
                //  $("#stopsValueInput").data("result",evt);
                document.getElementById('stopsNameLabel').value = data.name;
                document.getElementById('stopsNameLabel').name = data.id;
                document.getElementById('stopsValueInput').value = data.name;
                document.getElementById('stopsValueInput').name = data.pageId;
                document.getElementById('stopsDescription').innerText = data.description;
                document.getElementById('stopsGroups').innerText = data.groups;
                document.getElementById('stopsBundel').innerText = data.bundel;
                document.getElementById('stopsVersion').innerText = data.version;
                document.getElementById('stopSowner').innerText = data.owner;
                document.getElementById('stopCreateDate').innerText = data.crtDttmString;

                /* document.getElementById('stopStateId').innerText = data.state ? data.state : "No state";
                 document.getElementById('stopsStartTimeId').innerText = data.startTimes ? data.startTimes : "No startTime";
                 document.getElementById('stopEndTimeId').innerText = data.stopTimes ? data.stopTimes : "No stopTimes";*/
                //成功的话去掉定时器
                window.clearTimeout(timerPath);
            } else {
                console.log("stops属性查询null");
                flag++;
                if (flag > 5) {
                    window.clearTimeout(timerPath);
                    return;
                }
                timerPath = window.setTimeout(queryStopsProperty(id), 500);
            }
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
            if ('0' != dataMap.code) {
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
                    $("#createDateID").html('createrTime：');
                    $("#updateStopNameBtn").hide();
                    document.getElementById('table_idDiv').style.display = 'none';
                    document.getElementById('stopsDescription').innerText = queryInfo.flow.name;
                    document.getElementById('stopsNameLabel').value = queryInfo.pageId;
                    document.getElementById('stopsGroups').innerText = queryInfo.inport;
                    document.getElementById('stopsBundel').innerText = queryInfo.outport;
                    document.getElementById('stopsVersion').innerText = queryInfo.stopFrom.name;
                    document.getElementById('stopSowner').innerText = queryInfo.stopTo.name;
                    document.getElementById('stopCreateDate').innerText = queryInfo.crtDttmString;
                }
            } else {
                console.log("path属性查询null");
            }
        }
    });
}

function add(data, stopId, isCheckpointss) {
    if (data != null && data.length > 0) {
        while (divValue.hasChildNodes()) {
            divValue.removeChild(divValue.firstChild);
        }
        var table = document.createElement("table");
        table.style.borderCollapse = "separate";
        table.style.borderSpacing = "0px 5px";
        table.style.marginLeft = "12px";
        var tbody = document.createElement("tbody");
        for (var y = 0; y < data.length; y++) {
            var select = document.createElement('select');
            select.style.width = "290px";
            select.style.height = "32px";
            select.setAttribute('id', '' + data[y].name + '');
            select.setAttribute('onblur', 'shiqu("' + data[y].id + '","' + data[y].name + '","select")');
            select.setAttribute('class', 'form-control');
            if (isExample){
                select.setAttribute('disabled', 'disabled');
            }
            var displayName = data[y].displayName;
            var customValue = data[y].customValue;
            var allowableValues = data[y].allowableValues;
            var isSelect = data[y].isSelect;
            //是否必填
            var required = data[y].required;
            //如果大于4并且isSelect为true表示有下拉框
            if (allowableValues.length > 4 && isSelect) {
                var selectValue = JSON.parse(allowableValues);
                var selectInfo = JSON.stringify(selectValue);
                var strs = selectInfo.split(",");
                var optionDefault = document.createElement("option");
                optionDefault.value = '';
                optionDefault.innerHTML = '';
                optionDefault.setAttribute('selected', 'selected');
                select.appendChild(optionDefault);
                //循环给select下来赋值
                for (i = 0; i < strs.length; i++) {
                    var option = document.createElement("option");
                    option.style.backgroundColor = "#DBDBDB";
                    option.value = strs[i].replace("\"", "").replace("\"", "").replace("\[", "").replace("\]", "");
                    option.innerHTML = strs[i].replace("\"", "").replace("\"", "").replace("\[", "").replace("\]", "");
                    //设置默认选中项
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
            displayName.setAttribute('onclick', 'stopTabTd(this)');
            displayName.style.width = "290px";
            displayName.setAttribute('readonly', 'readonly');
            displayName.style.background = "rgb(245, 245, 245)";
            customValue = customValue == 'null' ? '' : customValue;
            displayName.setAttribute('value', '' + customValue + '');
            var spanDisplayName = 'span' + data[y].displayName;
            var spanDisplayName = document.createElement('span');
            var spanFlag = document.createElement('span');
            spanFlag.setAttribute('style', 'color:red');
            mxUtils.write(spanDisplayName, '' + data[y].name + '' + ": ");
            mxUtils.write(spanFlag, '*');
            //端口不可编辑
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
            //开始appendchild()追加各个元素
            td.appendChild(spanDisplayName);
            td3.appendChild(img);
            //本次循环大于4追加下拉,小于4默认文本框
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
            //创建一个数组
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
        if (isCheckpointss) {
            checkboxCheckpoint.setAttribute('checked', 'checked');
        }
        checkboxCheckpoint.setAttribute('onclick', 'saveCheckpoints("' + stopId + '")');
        var spanCheckpoint = document.createElement('span');
        mxUtils.write(spanCheckpoint, '是否加Checkpoint');
        btn.style.width = '202px';
        btn.style.marginLeft = '18px';
        btn.style.marginTop = '10px';
        divValue.appendChild(table);
        //divValue.appendChild(btn);
        divValue.appendChild(checkboxCheckpoint);
        divValue.appendChild(spanCheckpoint);
    }
}

// 添加操作处理
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

//保存xml文件和相关信息
function saveXml(paths, operType) {
    var getXml = thisEditor.getGraphXml();
    var sssss = getXml.outerHTML;
    //var waitxml = encodeURIComponent(getXml.outerHTML);//这就是要提交到后台的xml代码

    $.ajax({
        cache: true,//保留缓存数据
        type: "POST",//为post请求
        url: "/piflow-web/grapheditor/saveData",//这是我在后台接受数据的文件名
        //data:$('#loginForm').serialize(),//将该表单序列化
        data: {
            imageXML: sssss,
            load: loadId,
            operType: operType
        },
        async: true,//设置成true，这标志着在请求开始后，其他代码依然能够执行。如果把这个选项设置成false，这意味着所有的请求都不再是异步的了，这也会导致浏览器被锁死
        error: function (request) {//请求失败之后的操作
            return;
        },
        success: function (data) {//请求成功之后的操作
            var dataMap = JSON.parse(data);
            if (0 !== dataMap.code) {
                console.log(operType + " save success");
                thisEditor.setModified(false);
                if (operType && '' !== operType) {
                    //获取port
                    getStopsPort(paths);
                }
            } else {
                alert(operType + " save fail");
                console.log(operType + " save fail");
                fullScreen.hide();
            }
        }
    });
}

//打开xml文件
function openXml() {
    $.ajax({
        cache: true,//保留缓存数据
        type: "POST",//为post请求
        url: "/piflow-web/flow/loadData",//这是我在后台接受数据的文件名
        //data:$('#loginForm').serialize(),//将该表单序列化
        async: true,//设置成true，这标志着在请求开始后，其他代码依然能够执行。如果把这个选项设置成false，这意味着所有的请求都不再是异步的了，这也会导致浏览器被锁死
        error: function (request) {//请求失败之后的操作
            return;
        },
        success: function (data) {//请求成功之后的操作
            loadXml(data);
            console.log("success");
        }
    });
}

//加载xml文件
function loadXml(loadStr) {
    var xml = mxUtils.parseXml(loadStr);
    var node = xml.documentElement;
    var dec = new mxCodec(node.ownerDocument);
    dec.decode(node, graphGlobal.getModel());
    eraseRecord()
}

//请求接口重新加载stops
function reloadStops() {
    fullScreen.show();
    $.ajax({
        data: {"load": loadId},
        cache: true,//保留缓存数据
        type: "POST",//为post请求
        url: "/piflow-web/grapheditor/reloadStops",//这是我在后台接受数据的文件名
        error: function (request) {//请求失败之后的操作
            fullScreen.hide();
            alert("reload fail");
            return;
        },
        success: function (data) {//请求成功之后的操作
            var dataMap = JSON.parse(data);
            if (0 !== dataMap.code) {
                window.location.href = "/piflow-web/grapheditor/home?load=" + dataMap.load + "&_" + new Date().getTime();
            } else {
                alert("reload fail");
                fullScreen.hide();
            }
        }
    });
}

function queryFlowInfo() {
    $.ajax({
        data: {"load": loadId},
        cache: true,//保留缓存数据
        type: "POST",//为post请求
        url: "/piflow-web/flow/queryFlowData",//这是我在后台接受数据的文件名
        async: true,//设置成true，这标志着在请求开始后，其他代码依然能够执行。如果把这个选项设置成false，这意味着所有的请求都不再是异步的了，这也会导致浏览器被锁死
        error: function (request) {//请求失败之后的操作
            return;
        },
        success: function (data) {//请求成功之后的操作
            var dataMap = JSON.parse(data);
            if (document.getElementById("UUID")) {//js判断元素是否存在
                var flow = dataMap.flow;
                if (flow != null && flow != "")
                    var flowInfoDbInfo = flow.flowInfoDbVo;
                if (flow != null && flow != "") {
                    document.getElementById('UUID').innerText = flow.uuid ? flow.uuid : "No content";
                    document.getElementById('flowName').innerText = flow.name ? flow.name : "No content";
                    document.getElementById('flowDescription').innerText = flow.description ? flow.description : "No content";
                    document.getElementById('createrTime').innerText = flow.crtDttmString ? flow.crtDttmString : "No content";
                } else {
                    document.getElementById('UUID').innerText = "No content";
                    document.getElementById('flowName').innerText = "No content";
                    document.getElementById('flowDescription').innerText = "No content";
                    document.getElementById('createrTime').innerText = "No content";
                }
            }
        }
    });
}

//运行
function runFlow() {
    fullScreen.show();
    $.ajax({
        cache: true,//保留缓存数据
        type: "POST",//为post请求
        url: "/piflow-web/flow/runFlow",//这是我在后台接受数据的文件名
        //data:$('#loginForm').serialize(),//将该表单序列化
        data: {flowId: loadId},
        async: true,//设置成true，这标志着在请求开始后，其他代码依然能够执行。如果把这个选项设置成false，这意味着所有的请求都不再是异步的了，这也会导致浏览器被锁死
        error: function (request) {//请求失败之后的操作
            alert("Request Failed");
            fullScreen.hide();
            return;
        },
        success: function (data) {//请求成功之后的操作
            //console.log("success");
            var dataMap = JSON.parse(data);
            if ('0' !== dataMap.code) {
                window.location.href = "/piflow-web/process/getProcessById?parentAccessPath=grapheditor&processId=" + dataMap.processId;
            } else {
                alert("启动失败：" + dataMap.errMsg);
            }
            fullScreen.hide();
        }
    });
}

//获取端口
function getStopsPort(paths) {
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
                    //alert(dataMap);
                    if ('1' === dataMap.code) {
                        var sourceType = dataMap.sourceType;
                        var targetType = dataMap.targetType;
                        var sourceTypeStr = sourceType.text;
                        var targetTypeStr = targetType.text;
                        //判断可用端口数，如果可用端口数不大于0则直接删除线
                        if (dataMap.sourceCounts > 0 && dataMap.targetCounts > 0) {
                            // 获取source端口的详细使用情况
                            var sourcePortUsageMap = dataMap.sourcePortUsageMap;
                            if (sourcePortUsageMap) {
                                $('#copyDivCheckbox').show();
                                for (portName in sourcePortUsageMap) {
                                    var portNameVal = sourcePortUsageMap[portName];
                                    var obj = $('#copyDivCheckbox').clone();
                                    $(obj).removeAttr("id");
                                    $(obj).attr('class', 'addCheckbox').attr('id', portName + 'Checkbox');
                                    $(obj).find('input').attr('class', 'addCheckbox').attr('id', portName).attr('name', portName).attr('value', portName).attr('checked', !portNameVal).attr('disabled', !portNameVal);
                                    $(obj).find('span').attr('class', 'addCheckbox').attr('class', portName).attr('disabled', !portNameVal);
                                    $('#sourceTitleCheckbox').append(obj);
                                    $('.' + portName).text(portName);
                                }
                                // 当类型为Any时，添加创建按钮
                                if ('Any' === sourceTypeStr) {
                                    $('#copyDivCrtBtn').show();
                                    var sourceCrtBtn = $('#copyDivCrtBtn').clone();
                                    $(sourceCrtBtn).removeAttr("id");
                                    $(sourceCrtBtn).attr('class', 'addCrtBtn input-group').attr('id', 'sourceCrtBtn');
                                    $(sourceCrtBtn).find('input').attr('class', 'addCrtBtn form-control').attr('id', 'sourceCrtPort');
                                    $(sourceCrtBtn).find('button').attr('class', 'addCrtBtn btn btn-default').attr('onclick', 'crtAnyPort("sourceCrtPort",true)');
                                    $('#sourceTitleBtn').append(sourceCrtBtn);
                                    $('#copyDivCrtBtn').hide();
                                }
                            } else {
                                // 当类型为Default时，添加默认端口
                                if ('Default' === sourceTypeStr) {
                                    $('#sourceTitleCheckbox').html('<input type="checkbox" class="addCheckbox" checked="checked" disabled="disabled"/><span class="addCheckbox">default</span>');
                                }
                            }
                            $('#sourceTypeDiv').html(sourceTypeStr);
                            $('#sourceTitle').show();
                            $('#copyDivCheckbox').hide();
                            $('#sourceTitleStr').html('Source:' + dataMap.sourceName);

                            // 获取target端口的详细使用情况
                            var targetPortUsageMap = dataMap.targetPortUsageMap;
                            if (targetPortUsageMap) {
                                $('#copyDivCheckbox').show();
                                for (portName in targetPortUsageMap) {
                                    var portNameVal = targetPortUsageMap[portName];
                                    var obj = $('#copyDivCheckbox').clone();
                                    $(obj).removeAttr("id");
                                    $(obj).attr('class', 'addCheckbox').attr('id', portName + 'Checkbox');
                                    $(obj).find('input').attr('class', 'addCheckbox').attr('id', portName).attr('name', portName).attr('value', portName).attr('checked', !portNameVal).attr('disabled', !portNameVal);
                                    $(obj).find('span').attr('class', 'addCheckbox').attr('class', portName).attr('disabled', !portNameVal);
                                    $('#targetTitleCheckbox').append(obj);
                                    $('.' + portName).text(portName);
                                }
                                // 当类型为Any时，添加创建按钮
                                if ('Any' === targetTypeStr) {
                                    $('#copyDivCrtBtn').show();
                                    var targetCrtBtn = $('#copyDivCrtBtn').clone();
                                    $(targetCrtBtn).removeAttr("id");
                                    $(targetCrtBtn).attr('class', 'addCrtBtn input-group').attr('id', 'targetCrtBtn');
                                    $(targetCrtBtn).find('input').attr('class', 'addCrtBtn form-control').attr('id', 'targetCrtPort');
                                    $(targetCrtBtn).find('button').attr('class', 'addCrtBtn btn btn-default').attr('onclick', 'crtAnyPort("targetCrtPort",false)');
                                    $('#targetTitleBtn').append(targetCrtBtn);
                                    $('#copyDivCrtBtn').hide();
                                }
                            } else {
                                // 当类型为Default时，添加默认端口
                                if ('Default' === targetTypeStr) {
                                    $('#targetTitleCheckbox').html('<input type="checkbox" class="addCheckbox" checked="checked" disabled="disabled"/><span class="addCheckbox">default</span>');
                                }
                            }
                            $('#targetTypeDiv').html(targetTypeStr);
                            $('#targetTitle').show();
                            $('#copyDivCheckbox').hide();
                            $('#targetTitleStr').html('Targer:' + dataMap.targetName);
                            if ("Default" === sourceTypeStr && "Default" === targetTypeStr) {
                                $('#myModalPort').modal('hide');
                            } else if ("None" === sourceTypeStr || "None" === targetTypeStr) {
                                $('#myModalPort').modal('hide');
                            } else {
                                $('#myModalPort').modal('show');
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

// 选择端口
function chooseProt(paths) {
    if (1 === paths) {
        var sourcePortVal = '';
        var targetPortVal = '';
        var sourceTypeDiv = $('#sourceTypeDiv');
        var targetTypeDiv = $("#targetTypeDiv");
        if (!sourceTypeDiv && !sourceTypeDiv) {
            alert("页面报错，请检查！");
            return false;
        }
        var sourceTitleCheckbox = $('#sourceTitleCheckbox');
        var targetTitleCheckbox = $("#targetTitleCheckbox");
        if (sourceTitleCheckbox) {
            var sourceDivType = sourceTypeDiv.html();
            if (sourceDivType === 'Default') {
                //Default类型不校验
            } else {
                var sourceEffCheckbox = [];
                sourceTitleCheckbox.find("input[type='checkbox']:checked").each(function () {
                    if ($(this).prop("disabled") == false) {
                        sourceEffCheckbox[sourceEffCheckbox.length] = $(this);
                    }
                });
                if (sourceEffCheckbox.length > 1) {
                    alert("sourcePort只能选一个");
                    return false;
                }
                if (sourceEffCheckbox < 1) {
                    alert("请选择sourcePort");
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
            alert("页面报错，请检查！");
            return false;
        }
        if (targetTitleCheckbox) {
            var targetDivType = targetTypeDiv.html();
            if (targetDivType === 'Default') {
                //Default类型不校验
            } else {
                var targetEffCheckbox = [];
                targetTitleCheckbox.find("input[type='checkbox']:checked").each(function () {
                    if ($(this).prop("disabled") == false) {
                        targetEffCheckbox[targetEffCheckbox.length] = $(this);
                    }
                });
                if (targetEffCheckbox.length > 1) {
                    alert("targetPort只能选一个");
                    return false;
                }
                if (targetEffCheckbox.length < 1) {
                    alert("请选择targetPort");
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
            alert("页面报错，请检查！");
            return false;
        }
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
                url: "/piflow-web/grapheditor/savePathsPort",
                data: {
                    "flowId": loadId,
                    "pathLineId": pathLineId,
                    "sourcePortVal": sourcePortVal,
                    "targetPortVal": targetPortVal,
                    "sourceId": sourceMxCellId,
                    "targetId": targetMxCellId
                },
                async: true,
                traditional: true,
                error: function (request) {
                    console.log("error");
                    return;
                },
                success: function (data) {
                    var dataMap = JSON.parse(data);
                    //alert(dataMap);
                    if ('1' === dataMap.code) {
                        //alert("端口选择保存成功");
                    } else {
                        alert("端口选择保存失败");
                        graphGlobal.removeCells(pathsCells);
                    }
                }
            });

        }
        //alert("提交");
    } else {
        graphGlobal.removeCells(pathsCells);
    }
    $('#myModalPort').modal('hide');
    $('.addCrtBtn').remove();
    $('.addCheckbox').remove();
    $('#sourceTitle').hide();
    $('#targetTitle').hide();
}

function crtAnyPort(crtProtInputId, isSource) {
    var crtProtInput = $('#' + crtProtInputId);
    var portNameVal = crtProtInput.val();
    if (portNameVal && '' !== portNameVal) {
        if (!document.getElementById(portNameVal)) {
            $('#copyDivCheckbox').show();
            var obj = $('#copyDivCheckbox').clone();
            $(obj).removeAttr("id");
            $(obj).attr('class', 'addCheckbox').attr('id', portNameVal + 'Checkbox');
            $(obj).find('input').attr('class', 'addCheckbox').attr('id', portNameVal).attr('name', portNameVal).attr('value', portNameVal);
            $(obj).find('span').attr('class', 'addCheckbox').attr('class', portNameVal);
            if (isSource) {
                $('#sourceTitleCheckbox').append(obj);
            } else {
                $('#targetTitleCheckbox').append(obj);
            }
            $('.' + portNameVal).text(portNameVal);
            $('#copyDivCheckbox').hide();
        } else {
            alert("端口名称被占用！！");
        }
    } else {
        alert("端口名不能为空");
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
            alert("标记Checkpoint失败");
            return;
        },
        success: function (data) {
            var dataMap = JSON.parse(data);
            //alert(dataMap);
            //alert("端口选择保存成功");
            //console.log("attribute update success");
            if ('1' === dataMap.code) {
                alert("修改标记Checkpoint成功");
            } else {
                alert("修改标记Checkpoint失败");
            }

        }
    });
}

function saveTemplate() {
    var getXml = thisEditor.getGraphXml();
    var sssss = getXml.outerHTML;
    layer.prompt({
        title: 'please enter the template name',
        formType: 0,
        btn: ['submit', 'cancel']
    }, function (text, index) {
        layer.close(index);
        $.ajax({
            cache: true,//保留缓存数据
            type: "POST",//为post请求
            url: "/piflow-web/template/saveTemplate",//这是我在后台接受数据的文件名
            data: {
                value: sssss,
                load: loadId,
                name: text
            },
            async: true,
            error: function (request) {//请求失败之后的操作
                console.log(" save template error");
                return;
            },
            success: function (data) {//请求成功之后的操作
                var dataMap = JSON.parse(data);
                if (0 !== dataMap.code) {
                    layer.msg(dataMap.errMsg, {icon: 1, shade: 0, time: 2000}, function () {
                    });
                } else {
                    layer.msg(dataMap.errMsg, {icon: 2, shade: 0, time: 2000}, function () {
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
        if (0 !== dataMap.code) {
            layer.msg(dataMap.errMsg, {icon: 1, shade: 0, time: 2000}, function () {
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
            oDiv.style.display = "block";

        }
    }
}

function loadingSelect() {
    $.ajax({
        url: "/piflow-web/template/templateAllSelect",
        type: "post",
        success: function (data) {
            var dataMap = JSON.parse(data);
            if (dataMap.code != '0') {
                var temPlateList = dataMap.temPlateList;
                $("#loadingXmlSelect").append("<option value='-1' >------------please choose------------</option>");
                for (var i = 0; i < temPlateList.length; i++) {
                    $("#loadingXmlSelect").append("<option value=" + temPlateList[i].id + " >" + temPlateList[i].name + "</option>");
                }
            }
        }
    });
}

function loadTemplate() {
    var id = $("#loadingXmlSelect").val();
    if (id == '-1') {
        return;
    }
    var name = $("#loadingXmlSelect option:selected").text();
    layer.confirm('Are you sure you want to load ' + name + '？', {
        btn: ['submit', 'cancel'] //按钮
    }, function () {
        loadingXml(id, loadId);
        var oDiv = document.getElementById("divloadingXml");
        oDiv.style.display = "none";
    }, function () {
        var oDiv = document.getElementById("divloadingXml");
        oDiv.style.display = "none";
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
            alert("This is an example, you can't add edit delete");
        } else if ('MOVED' === operType) {
            findBasicInfo(evt);
        }
        prohibitEditing(isExample, operType);
    }

}

function prohibitEditing(isExample, operType) {
    $.ajax({
        cache: true,//保留缓存数据
        type: "POST",//为post请求
        url: "/piflow-web/exampleMenu/exampleUrlList",//这是我在后台接受数据的文件名
        data: {},
        async: true,
        error: function (request) {//请求失败之后的操作
            if ('ADD' === operType || 'REMOVED' === operType) {
                location.reload();
            }
            eraseRecord()
            return;
        },
        success: function (data) {//请求成功之后的操作
            if ('ADD' === operType || 'REMOVED' === operType) {
                location.reload();
            }
            eraseRecord()
        }
    });
}

//擦除画板记录
function eraseRecord() {
    thisEditor.lastSnapshot = new Date().getTime();
    thisEditor.undoManager.clear();
    thisEditor.ignoredChanges = 0;
    thisEditor.setModified(false);
}
