// 正则验证
jQuery.validator.addMethod("regex", function(value, element, params) {
	var exp = new RegExp(params);
	return this.optional(element) || exp.test(value);
}, "格式错误");
jQuery.validator.addMethod("regexHtml", function(value, element, params) {
	var exp = null;
	if (/^(\^.+\$)(.+)|(.+)\$(.+)$/.test(params[0])) {
		var attr = /^(\^.+\$)(.+)|(.+)\$(.+)$/g.exec(params[0]);
		exp = new RegExp(attr[1], attr[2]);
	} else {
		var attr = /^\^.+\$|.+$/g.exec(params[0]);
		exp = new RegExp(attr[0]);
	}
	return this.optional(element) || exp.test(value);
}, $.validator.format("{1}"));
// 手机号码验证
jQuery.validator.addMethod("mobile", function(value, element) {
	var length = value.length;
	var mobile = /^1\d{10}$/;
	return this.optional(element) || (length == 11 && mobile.test(value));
}, "手机号码格式错误");
// 电话号码验证
jQuery.validator.addMethod("phone", function(value, element) {
	var tel = /^0[0-9]{2,3}\-?[2-9][0-9]{6,7}$/;
	return this.optional(element) || (tel.test(value));
}, "电话号码格式错误");
// 邮政编码验证
jQuery.validator.addMethod("zipCode", function(value, element) {
	var tel = /^[0-9]{6}$/;
	return this.optional(element) || (tel.test(value));
}, "邮政编码格式错误");
// QQ号码验证
jQuery.validator.addMethod("qq", function(value, element) {
	var tel = /^[1-9]\d{4,9}$/;
	return this.optional(element) || (tel.test(value));
}, "qq号码格式错误");
// IP地址验证
jQuery.validator
		.addMethod(
				"ip",
				function(value, element) {
					var ip = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
					return this.optional(element)
							|| (ip.test(value) && (RegExp.$1 < 256
									&& RegExp.$2 < 256 && RegExp.$3 < 256 && RegExp.$4 < 256));
				}, "Ip地址格式错误");
// 字母和数字的验证
jQuery.validator.addMethod("chrnum", function(value, element) {
	var chrnum = /^([a-zA-Z0-9]+)$/;
	return this.optional(element) || (chrnum.test(value));
}, "只能输入数字和字母(字符A-Z, a-z, 0-9)");
// 字母和数字的验证_
jQuery.validator.addMethod("chrnum_", function(value, element) {
	var chrnum = /^(\w+)$/;
	return this.optional(element) || (chrnum.test(value));
}, "只能输入数字和字母(字符A-Z, a-z, 0-9,_)");
// 字母和数字的验证_.
jQuery.validator.addMethod("chrnum_extra", function(value, element) {
	var chrnum = /^([\w\.]+)$/;
	return this.optional(element) || (chrnum.test(value));
}, "只能输入数字和字母(字符A-Z, a-z, 0-9,_,.)");
// 字母、数字和汉字的验证
jQuery.validator.addMethod("chrnumChinese", function(value, element) {
	var chrnum = /^([\u4e00-\u9fa5a-zA-Z0-9]+)$/;
	return this.optional(element) || (chrnum.test(value));
}, "只能输入汉字、数字和字母(汉字、字符A-Z, a-z, 0-9)");
// 字母、数字和汉字的验证_
jQuery.validator.addMethod("chrnumChinese_", function(value, element) {
	var chrnum = /^([\u4e00-\u9fa5\w]+)$/;
	return this.optional(element) || (chrnum.test(value));
}, "只能输入汉字、数字和字母(汉字、字符A-Z, a-z, 0-9)");
// 中文的验证
jQuery.validator.addMethod("chinese", function(value, element) {
	var chinese = /^[\u4e00-\u9fa5]+$/;
	return this.optional(element) || (chinese.test(value));
}, "只能输入中文");
// 下拉框验证
$.validator.addMethod("selectNone", function(value, element) {
	return value == "请选择";
}, "必须选择一项");
// 字节长度验证
jQuery.validator.addMethod("byteRangeLength",
		function(value, element, param) {
			var length = value.length;
			for ( var i = 0; i < value.length; i++) {
				if (value.charCodeAt(i) > 127) {
					length++;
				}
			}
			return this.optional(element)
					|| (length >= param[0] && length <= param[1]);
		}, $.validator.format("请确保输入的值在{0}-{1}个字节之间(一个中文字算2个字节)"));

jQuery.validator.addMethod("mobilePhone", function(value, element) {
	return this.optional(element)
			|| /^((\d{3,4})|\d{3,4}-)?\d{5,8}(-\d+)*$/.test(value);
}, "请正确填写您的电话号码");

jQuery.validator
		.addMethod(
				"url2",
				function(value, element) {
					return this.optional(element)
							|| /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)*(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i
									.test(value);
				}, "请输入正确的URL地址");
// 字母、数字、小数点和汉字的验证
jQuery.validator.addMethod("chrnumChinese2", function(value, element) {
	var chrnum = /^([\u4e00-\u9fa5a-zA-Z0-9\.]+)$/;
	return this.optional(element) || (chrnum.test(value));
}, "只能输入汉字、数字、小数点和字母(汉字、字符A-Z, ., a-z, 0-9)");

// 字母、数字、斜杠和汉字的验证
jQuery.validator.addMethod("chrnumChinesesSprit", function(value, element) {
	var chrnum = /^([\u4e00-\u9fa5a-zA-Z0-9\/]+)$/;
	return this.optional(element) || (chrnum.test(value));
}, "只能输入汉字、数字、字母和斜杠(汉字、字符A-Z, a-z, 0-9,/)");

// 大小写英文字母
jQuery.validator.addMethod("enchar", function(value, element) {
	var chr = /^([a-zA-Z]*)$/;
	return this.optional(element) || (chr.test(value));
}, "只能输入大写英文字母");
