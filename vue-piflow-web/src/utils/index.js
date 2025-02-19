/**
 * @description 保留几位小数
 * @param {Number} num 数字
 * @param {Number} places 保留几位
 */
export const toFixed = (num, places = 2) => {
    places = Math.pow(10, places)
    return Math.round(num * places) / places
  }

  
  /**
* description 流量单位转换
* @param { Number } val传入值
* @param { String } type类型
* @param { String } fixed保留位数
* @returns { Number, String } 传入type(单位)返回数值，未传自动计算范围 string
*/
export const formatFlow = (val, type, fixed = 2) => {
    if (!val) return '0B'
    switch (type) {
      case 'GB': return toFixed(val / 1024 / 1024 / 1024, fixed)
      case 'MB': return toFixed(val / 1024 / 1024, fixed)
      case 'KB': return toFixed(val / 1024, fixed)
      case 'B': return toFixed(val, fixed)
      default: {
        const gb = toFixed(val / 1024 / 1024 / 1024, fixed)
        const mb = toFixed(val / 1024 / 1024, fixed)
        const kb = toFixed(val / 1024, fixed)
        if (val < 1024) {
          return val + 'B'
        } else if (kb < 1024) {
          return kb + 'KB'
        } else if (mb < 1024) {
          return mb + 'MB'
        } else {
          return gb + 'GB'
        }
      }
    }
  }

export const  validatePassword = (password) => {
    // 正则表达式解释：
    // (?=.*[a-z]) - 至少一个小写字母
    // (?=.*[A-Z]) - 至少一个大写字母
    // (?=.*\d) - 至少一个数字
    // (?=.*[@$!%*?&]) - 至少一个特殊字符
    // .{9,} - 至少9个字符（因为长度要大于8）
    const regex = /(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[^a-zA-Z0-9]).{8,30}/;
    
    return regex.test(password);
}


 

export const defaultChangePsdDays = 30 *  24 * 60 * 60 * 1000

export const validateTime = (time)=>{
  const  now = new Date().getTime()
  const last = new Date(time).getTime()

  return now > last + defaultChangePsdDays
}
