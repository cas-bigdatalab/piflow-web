import CryptoJS from "crypto-js";
//AES加密
const key = "ABCDEFGHIJKL_key";
const iv = "ABCDEFGHIJKLM_iv";
export const aesMinEncrypt = function(word) {
    var _word = CryptoJS.enc.Utf8.parse(word),
        _key = CryptoJS.enc.Utf8.parse(key),
        _iv = CryptoJS.enc.Utf8.parse(iv);
    var encrypted = CryptoJS.AES.encrypt(_word, _key, {
        iv: _iv,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
    });
    return encrypted.toString();
}
