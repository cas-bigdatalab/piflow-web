
// 去除空值
export  const handleFormata = (data)=>{
    if(!data) return 
    const obj = JSON.parse(JSON.stringify(data))
    for (const key in obj) {
        if(Object.prototype.toString.call(obj[key]) === '[object Object]'){
            obj[key] =  handleFormata(obj[key])
        }else{
            if(!(!!obj[key] || obj[key] === 0|| obj[key] === false) ){
                delete obj[key]
            }
        }
    }
    return obj
}



// 深度合并
export  const handleDeepMerge = (base,data)=>{
    for (const key in base) {
        if(Object.prototype.toString.call(base[key]) === '[object Object]'){
            base[key] =  handleDeepMerge(base[key],data[key])
        }else{
            if(data[key] || data[key] ===  0 || data[key] === false ){
                base[key] = data[key]
            }
        }
    }
    return base
}

