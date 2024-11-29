import axios from 'axios';

// 根据FlowId获取组件列表
export function getStopsInfoByFlowId (id) {
    return axios({
        method:'get',
        url:`/stops/getStopsInfoByFlowId?flowId=${id}`,
    })
}
