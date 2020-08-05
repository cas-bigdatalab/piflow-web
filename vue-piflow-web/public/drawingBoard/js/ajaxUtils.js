// var web_header_prefix = "http://10.0.88.46:86/piflow";
var web_base_origin = window.location.origin;
var sever_base_origin = "http://10.0.85.80:6002/piflow-web";
var basePath = window.sessionStorage.getItem("basePath")
var web_header_prefix = basePath.indexOf(window.location.origin) > -1 ? basePath : web_base_origin + basePath; //与 .env.production 内容同步
var token = window.sessionStorage.getItem('token') //此处放置请求到的用户token

/**
 * ajax工具js
 * @param requestType 请求类型(get,post)
 * @param url 请求url
 * @param async 是否异步
 * @param requestData 请求参数
 * @param backFunc 请求成功后回调方法
 * @param errBackFunc 请求失败后回调方法
 */
function ajaxRequest(param) {
    if (!param.url) {
        throw "url is null"
    }
    var requestType = param.type ? param.type : "GET";
    var url = param.url;
    var cache = param.cache ? true : false;
    var async = param.async ? true : false;
    var traditional = param.traditional ? true : false;
    var requestData = param.data ? param.data : {};
    var backFunc = param.success;
    var errBackFunc = param.error;
    var processData = true;
    if (undefined !== param.processData) {
        processData = param.processData ? true : false;
    }
    var async = true;
    if (undefined !== param.async) {
        async = param.async ? true : false;
    }
    var contentType = "application/x-www-form-urlencoded;charset=UTF-8";
    if (undefined !== param.contentType) {
        contentType = param.contentType
    }
    $.ajax({
        cache: cache,
        type: requestType,
        async: async,
        url: web_header_prefix + url,
        data: requestData,
        traditional: traditional,
        contentType: contentType,
        processData: processData,
        headers: {
            Authorization: ("Bearer " + token)
        },
        // dataType: 'json',
        // beforeSend: function (request) {
        //     request.setRequestHeader("token", tokenInfo);
        // },
        // xhrFields: {
        //     withCredentials: true
        // },
        success: function (data) {
            //data =  JSON.parse(data);
            if (data.code === 403 || data.code === 401) {
                //  alert(data.errMsg);
                // console.log(data);
                window.location.href = web_base_origin + "/#/login";
                return;
            }
            if (backFunc && $.isFunction(backFunc)) {
                backFunc(data);
            }
        },
        error: function (request) {//请求失败之后的操作
            if (errBackFunc && $.isFunction(errBackFunc)) {
                errBackFunc(request);
            }
            return;
        }
    });
};

function ajaxLoad(elementId, requestUrl, backFunc, errBackFunc) {
    $.ajax({
        type: "GET",
        async: true,
        url: '/drawingBoard' + requestUrl,
        headers: {
            Authorization: ("Bearer " + token)
        },
        success: function (data) {
            //data =  JSON.parse(data);
            // if (data.code === 403 || data.code === 401) {
            //     //  alert(data.errMsg);
            //     console.log(data);
            //     // window.location.href = (web_header_prefix + "/login");
            //     return;
            // }
            $("#" + elementId).html(data);
            if (backFunc && $.isFunction(backFunc)) {
                backFunc(data);
            }
        },
        error: function (request) {//请求失败之后的操作
            if (errBackFunc && $.isFunction(errBackFunc)) {
                errBackFunc(request);
            }
            return;
        }
    });
    // if (backFunc && $.isFunction(backFunc)) {
    //     $("#" + elementId).load(web_header_prefix + requestUrl, backFunc());
    // } else {
    //     $("#" + elementId).load(web_header_prefix + requestUrl);
    // }
}

function getUrlParams(url) {
    var result = new Object();
    var idx = url.lastIndexOf('?');
    // console.log(url);
    if (idx > 0) {
        var params = url.substring(idx + 1).split('&');

        for (var i = 0; i < params.length; i++) {
            idx = params[i].indexOf('=');

            if (idx > 0) {
                result[params[i].substring(0, idx)] = params[i].substring(idx + 1);
            }
        }
    }

    return result;
}

function openLayerWindowLoadHtml(htmlStr, window_width, window_height, title) {
    layer.open({
        type: 1,
        title: '<span style="color: #269252;">' + title + '</span>',
        shade: 0,
        shadeClose: false,
        closeBtn: 1,
        shift: 7,
        area: [window_width + 'px', window_height + 'px'], //Width height
        skin: 'layui-layer-rim', //Add borders
        content: htmlStr
    });
}

function openLayerWindowLoadUrl(url, window_width, window_height, title) {
    layer.open({
        type: 2,
        title: '<span style="color: #269252;">' + title + '</span>',
        shade: 0,
        shadeClose: false,
        closeBtn: 1,
        shift: 7,
        area: [window_width + 'px', window_height + 'px'], //Width height
        skin: 'layui-layer-rim', //Add borders
        content: url
    });
}

// window.location
function window_location_href(url) {
    window.location.href = window.location.origin + "/#/drawingBoard?src=" + url;
}

function new_window_open(url) {

    var tempWindow = window.open(window.location.origin + "/#/drawingBoard?src=" + url);
    if (tempWindow == null || typeof (tempWindow) == 'undefined') {
        alert('The window cannot be opened. Please check your browser settings.')
    }

}
