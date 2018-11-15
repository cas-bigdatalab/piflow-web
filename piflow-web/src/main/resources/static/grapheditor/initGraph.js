// Extends EditorUi to update I/O action states based on availability of backend
var graphGlobal = null;
var thisEditor = null;
var sign = true;
(function()
{

    var editorUiInit = EditorUi.prototype.init;

    EditorUi.prototype.init = function()
    {
        editorUiInit.apply(this, arguments);
        this.actions.get('export').setEnabled(false);
        graphGlobal = this.editor.graph;
        thisEditor = this.editor;

        // Updates action states which require a backend
        if (!Editor.useLocalStorage)
        {
            mxUtils.post(OPEN_URL, '', mxUtils.bind(this, function(req)
            {
                var enabled = req.getStatus() != 404;
                this.actions.get('open').setEnabled(enabled || Graph.fileSupport);
                this.actions.get('import').setEnabled(enabled || Graph.fileSupport);
                this.actions.get('save').setEnabled(enabled);
                this.actions.get('saveAs').setEnabled(enabled);
                this.actions.get('export').setEnabled(enabled);
            }));
        }
        //监控事件
        graphGlobal.addListener(mxEvent.CELLS_ADDED, function(sender, evt) {
            var cells = evt.properties.cells;
            addCellsCustom(cells,'ADD');
        });
        graphGlobal.addListener(mxEvent.CELLS_MOVED, function(sender, evt) {
            saveXml(null,'MOVED');
        });
        graphGlobal.addListener(mxEvent.CELLS_REMOVED, function(sender, evt) {
            saveXml(null,'REMOVED');
        });
        graphGlobal.addListener(mxEvent.CLICK, function(sender, evt) {
            findBasicInfo(evt);
        });
        if(xmlDate){
            var xml = mxUtils.parseXml(xmlDate);
            var node = xml.documentElement;
            var dec = new mxCodec(node.ownerDocument);
            dec.decode(node, graphGlobal.getModel());
            thisEditor.lastSnapshot = new Date().getTime();
            thisEditor.undoManager.clear();
            thisEditor.ignoredChanges = 0;
            thisEditor.setModified(false);
        };
        graphGlobal.setTooltips(false);
    };
    // Adds required resources (disables loading of fallback properties, this can only
    // be used if we know that all keys are defined in the language specific file)
    mxResources.loadDefaultBundle = false;
    var bundle = mxResources.getDefaultBundle(RESOURCE_BASE, mxLanguage) ||
        mxResources.getSpecialBundle(RESOURCE_BASE, mxLanguage);

    // Fixes possible asynchronous requests
    mxUtils.getAll([bundle, STYLE_PATH + '/default.xml'], function(xhr)
    {
        // Adds bundle text to resources
        mxResources.parse(xhr[0].getText());

        // Configures the default graph theme
        var themes = new Object();
        themes[Graph.prototype.defaultThemeName] = xhr[1].getDocumentElement();

        // Main
        new EditorUi(new Editor(urlParams['chrome'] == '0', themes));
    }, function()
    {
        document.body.innerHTML = '<center style="margin-top:10%;">Error loading resource files. Please check browser console.</center>';
    });
})();