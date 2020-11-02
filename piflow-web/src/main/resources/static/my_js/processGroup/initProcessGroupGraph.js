// Extends EditorUi to update I/O action states based on availability of backend
var graphGlobal = null;
var thisEditor = null;
var flag = 0;
var index = true
var nodePageIdAndStateList, xmlDate, processType, appId, processState, processId,stdoutLog,stderrLog;

// init crumbs
function initCrumbs(parentAccessPath) {
    if (parentAccessPath) {
        switch (parentAccessPath) {
            case "flow":
                $("#web_processList_navigation").hide();
                $("#web_flowList_navigation").show();
                $("#grapheditor_home_navigation").show();
                break;
            case "flowProcess":
                $("#web_processList_navigation").hide();
                $("#web_groupTypeProcessList_navigation").show();
                break;
            case "processGroupList":
                $("#web_processList_navigation").hide();
                $("#web_processGroupList_navigation").show();
                $("#web_getProcessGroupById_navigation").show();
                break;
        }
    }
}

// init right slider style
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

// get processGroup DrawingBoard data
function initProcessGroupDrawingBoardData(loadId, parentAccessPath, backFunc) {
    $('#fullScreen').show();
    ajaxRequest({
        cache: true,//Keep cached data
        type: "get",//Request for get
        url: "/processGroup/drawingBoardData",//This is the name of the file where I receive data in the background.
        //data:$('#loginForm').serialize(),//Serialize the form
        data: {
            loadId: loadId,
            parentAccessPath: parentAccessPath
        },
        async: false,//Setting it to true indicates that other code can still be executed after the request has started. If this option is set to false, it means that all requests are no longer asynchronous, which also causes the browser to be locked.
        error: function (request) {//Operation after request failure
            // window.location.href = (web_baseUrl + "/error/404");
            return;
        },
        success: function (data) {//Operation after request successful
            var dataMap = JSON.parse(data);
            console.log(dataMap);
            if (200 === dataMap.code) {
                processId = dataMap.processId;
                processType = dataMap.processType;
                nodePageIdAndStateList = dataMap.nodePageIdAndStateList;
                xmlDate = dataMap.xmlDate;
                parentsId = dataMap.processGroupId;
                appId = dataMap.appId;
                processState = dataMap.processState;
                $("#progress").text(dataMap.percentage);
                var getCheckpointParam = "'" + (dataMap.pID ? dataMap.pID : "") + "','" + (dataMap.parentProcessId ? dataMap.parentProcessId : "") + "', '" + (dataMap.processId ? dataMap.processId : "") + "'";
                $("#runFlow").attr("onclick", "getCheckpoint(" + getCheckpointParam + ")");
                $("#debugFlow").attr("onclick", "getCheckpoint(" + getCheckpointParam + ",'DEBUG')");
                $("#run_checkpoint_new").attr("onclick", "getCheckpoint(" + getCheckpointParam + ")");
                $("#debug_checkpoint_new").attr("onclick", "getCheckpoint(" + getCheckpointParam + ",'DEBUG')");
            } else {
                //window.location.href = (web_baseUrl + "/error/404");
            }
            $('#fullScreen').hide();
            if (backFunc && $.isFunction(backFunc)) {
                backFunc(data);
            }
        }
    });
}

// init processGroup mxGraph
function initProcessGroupGraph() {
    $("#right-group-wrap")[0].style.display = "block";
    $("#precessGroup-run")[0].style.display = "block";
    Format.noEditing(true);
    //EditorUi.prototype.noEditing = true;
    EditorUi.prototype.menubarHeight = 48;
    EditorUi.prototype.menubarShow = false;
    EditorUi.prototype.customToobar = true;
    var editorUiInit = EditorUi.prototype.init;
    EditorUi.prototype.init = function () {
        editorUiInit.apply(this, arguments);
        graphGlobal = this.editor.graph;
        thisEditor = this.editor;
        this.actions.get('export').setEnabled(false);
        graphGlobal.addListener(mxEvent.CLICK, function (sender, evt) {
            processGroupMxEventClick(evt.properties.cell)
        });
        graphGlobal.addListener(mxEvent.SIZE, function (sender, evt) {
            changIconTranslate();
        });
        graphGlobal.addListener(mxEvent.DOUBLE_CLICK, function (sender, evt) {
            OpenTheMonitorArtBoard(evt);
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
    ClickSlider();
    setTimeout(() => {
        console.log("svg_element");
        initMonitorIcon();
    }, 300)
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

// init Monitor Icon
function initMonitorIcon() {
    var svg_element = document.getElementsByClassName('geDiagramBackdrop geDiagramContainer')[0].getElementsByTagName("svg")[0];
    nodePageIdAndStateList.forEach(item => {
        var img_element_init = document.createElementNS("http://www.w3.org/2000/svg", "image");
        img_element_init.setAttribute("x", 0);
        img_element_init.setAttribute("y", 0);
        img_element_init.setAttribute("width", 20);
        img_element_init.setAttribute("height", 20);
        img_element_init.setAttribute("PiFlow_IMG", "IMG");
        img_element_init.href.baseVal = web_drawingBoard + "/img/Loading.gif";
        img_element_init.setAttribute("id", "stopLoadingShow" + item.pageId);

        var img_element_ok = document.createElementNS("http://www.w3.org/2000/svg", "image");
        img_element_ok.setAttribute("x", 0);
        img_element_ok.setAttribute("y", 0);
        img_element_ok.setAttribute("width", 20);
        img_element_ok.setAttribute("height", 20);
        img_element_ok.setAttribute("PiFlow_IMG", "IMG");
        img_element_ok.href.baseVal = web_drawingBoard + "/img/Ok.png";
        img_element_ok.setAttribute("id", "stopOkShow" + item.pageId);

        var img_element_fail = document.createElementNS("http://www.w3.org/2000/svg", "image");
        img_element_fail.setAttribute("x", 0);
        img_element_fail.setAttribute("y", 0);
        img_element_fail.setAttribute("width", 20);
        img_element_fail.setAttribute("height", 20);
        img_element_fail.setAttribute("PiFlow_IMG", "IMG");
        img_element_fail.href.baseVal = web_drawingBoard + "/img/Fail.png";
        img_element_fail.setAttribute("id", "stopFailShow" + item.pageId);
        img_element_init.style.display = "none";
        img_element_fail.style.display = "none";
        img_element_ok.style.display = "none";
        if (item.state) {
            if (item.state !== "INIT") {
                //stopImgChange.attr('opacity', 1);
                if (item.state && (item.state === "STARTED")) {
                    img_element_init.style.display = "block";
                    img_element_fail.style.display = "none";
                    img_element_ok.style.display = "none";
                } else if (item.state && item.state === "COMPLETED") {
                    img_element_init.style.display = "none";
                    img_element_fail.style.display = "none";
                    img_element_ok.style.display = "block";
                } else if (item.state && item.state === "FAILED") {
                    img_element_init.style.display = "none";
                    img_element_fail.style.display = "block";
                    img_element_ok.style.display = "none";
                } else if (item.state && item.state === "KILLED") {
                    img_element_init.style.display = "none";
                    img_element_fail.style.display = "block";
                    img_element_ok.style.display = "none";
                }
            }
        }

        if (svg_element && img_element_init && img_element_ok && img_element_fail) {
            var g_element = document.createElementNS("http://www.w3.org/2000/svg", "g");
            g_element.appendChild(img_element_init);
            g_element.appendChild(img_element_ok);
            g_element.appendChild(img_element_fail);
            svg_element.append(g_element);
        }
    });
    changIconTranslate();
}

// chang icon translate
function changIconTranslate() {
    var iconPositionElementArr = document.querySelectorAll("div[style='display: inline-block; font-size: 1px; font-family: PiFlow; color: #FFFFFF; line-height: 1.2; pointer-events: all; white-space: nowrap; ']");
    if (iconPositionElementArr && iconPositionElementArr.length > 0) {
        var iconPositionArr = {};
        iconPositionElementArr.forEach(item => {
            var x_y_div = item.parentElement.parentElement.style;
            var x_Position = x_y_div['margin-left'].replace("px", "");
            var y_Position = x_y_div['padding-top'].replace("px", "");
            iconPositionArr['stopLoadingShow' + item.textContent] = {x: x_Position, y: y_Position};
            iconPositionArr['stopFailShow' + item.textContent] = {x: x_Position, y: y_Position};
            iconPositionArr['stopOkShow' + item.textContent] = {x: x_Position, y: y_Position};
        });
        var imgsArr = document.querySelectorAll("image[PiFlow_IMG='IMG']");
        imgsArr.forEach(item => {
            var iconPosition = iconPositionArr[item.id]
            if (iconPosition) {
                item.setAttribute("transform", "translate(" + iconPosition.x + "," + iconPosition.y + ")");
            }
        });
    }
}

//Erase drawing board records
function eraseRecord() {
    thisEditor.lastSnapshot = new Date().getTime();
    thisEditor.undoManager.clear();
    thisEditor.ignoredChanges = 0;
    thisEditor.setModified(false);
}

//Double-click monitoring events
function OpenTheMonitorArtBoard(evt) {
    var cellFor = evt.properties.cell;
    if (cellFor.style && (cellFor.style).indexOf("text\;") === 0) {
    } else {
        ajaxRequest({
            cache: true,//Keep cached data
            type: "POST",//Request type post
            url: "/processGroup/getProcessIdByPageId",//This is the name of the file where I receive data in the background.
            data: {
                processGroupId: loadId,
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
                        if ('flow' === dataMap.nodeType) {
                            window_location_href("/page/process/mxGraph/index.html?drawingBoardType=PROCESS&parentAccessPath=processGroupList&processType=PROCESS&load=" + dataMap.processId);
                        } else if ('flowGroup' === dataMap.nodeType) {
                            window_location_href("/page/processGroup/mxGraph/index.html?drawingBoardType=PROCESS&parentAccessPath=processGroupList&processType=PROCESS_GROUP&load=" + dataMap.processGroupId);
                        }
                    }
                } else {
                    layer.msg(' failed', {icon: 2, shade: 0, time: 1000});
                }
            }
        });
    }
}

// processGroup mxEventClick
function processGroupMxEventClick(cell) {
    if (index) {
        $(".right-group").toggleClass("open-right");
        $(".ExpandSidebar").toggleClass("ExpandSidebar-open");
        index = false
    }

    if (cell && cell.style && (cell.style).indexOf("image\;") === 0 && processType == "GROUP") {
        //node info
        queryNodeInfo(loadId, cell.id);

    } else if (cell == undefined || cell && cell.style && (cell.style).indexOf("text\;") === 0) {
        // ProcessGroup info
        queryProcessGroup(loadId);
    } else {
        // path
        queryPathInfo(loadId, cell.id);
    }

}

// query processGroup info
function queryProcessGroup(loadId) {
    ajaxRequest({
        cache: true,
        type: "POST",
        url: "/processGroup/queryProcessGroup",
        data: {processGroupId: loadId},
        async: true,
        error: function (request) {
            return;
        },
        success: function (data) {
            $("#right-group")[0].innerHTML = data
        }
    });
}

// query node info
function queryNodeInfo(loadId, pageId) {
    ajaxRequest({
        cache: true,
        type: "POST",
        url: "/processGroup/queryProcess",
        data: {
            pageId: pageId,
            processGroupId: loadId
        },
        async: true,
        error: function (request) {
            return;
        },
        success: function (data) {
            $("#right-group")[0].innerHTML = data
        }
    });
}

// query path info
function queryPathInfo(loadId, pageId) {
    ajaxRequest({
        cache: true,
        type: "POST",
        url: "/processGroup/queryProcessGroupPath",
        data: {
            pageId: pageId,
            processGroupId: loadId
        },
        async: true,
        error: function (request) {
            return;
        },
        success: function (data) {
            $("#right-group")[0].innerHTML = data
        }
    })
}

window.onresize = function (e) {
    setTimeout(() => {
        changIconTranslate()
    }, 300);
}