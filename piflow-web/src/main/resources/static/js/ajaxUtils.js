var web_header_prefix = "/piflow-web";
var prefixHttpUrl = location.protocol + "//" + location.hostname + ':8001';
var rbacUrl = location.protocol + '//10.0.88.137:81';


var token = localStorage.getItem("token");


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
    $.ajax({
        cache: cache,
        type: requestType,
        async: async,
        url: web_header_prefix + url,
        data: requestData,
        traditional: traditional,
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
            // if (data.code === 403) {
            //     //  alert(data.errMsg);
            //     window.location.href = '/login';
            //     return;
            // }
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

function getUrlParams (url) {
    var result = new Object();
    var idx = url.lastIndexOf('?');

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

