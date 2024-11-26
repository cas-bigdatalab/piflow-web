import axios from 'axios';

// 获取用户类型列表
export function getEcosystemType () {
    return axios({
        method:'get',
        url:'/ecosystemType/get',
    })
}


// 获取用户信息
export function getByUsername () {
    return axios({
        method:'get',
        url:'/sysUser/getByUsername',
    })
}


// 更新用户信息
export function updateSysUser (data) {
    return axios({
        method:'post',
        url:'/sysUser/update',
        data
    })
}

// 获取用户列表信息
export function getUserListPage (params) {
    return axios({
        method:'get',
        url:'/user/getUserListPage',
        params
    })
}
// 新增用户
export function addUser (data) {
    return axios({
        method:'post',
        url:'/user/addUser',
        data
    })
}
// 新增用户
export function updateUser (data) {
    return axios({
        method:'post',
        url:'/user/updateUser',
        data
    })
}
// 删除用户
export function delUser (id) {
    return axios({
        method:'get',
        url:'/user/delUser?sysUserId='+id,
    })
}
// 获取所有角色
export function getAllRole () {
    return axios({
        method:'get',
        url:'/user/getAllRole',
    })
}
// 分配角色
export function updateRole (data) {
    return axios({
        method:'post',
        url:'/user/updateRole',
        data
    })
}

