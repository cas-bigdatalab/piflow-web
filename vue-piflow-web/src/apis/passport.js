import axios from "axios";


export const getRedirect = (data)=>{
    return axios({
        method:'get',
        url:'/passport/getRedirect?deviceId='+data,
    })
}


export const getStatus = (data)=>{
    return axios({
        method:'get',
        url:'/passport/getStatus?deviceId='+data,
    })
}
