let Mock = require('mockjs');

const data = Mock.mock({
    'status': 200,
    'dataSource|1-9': [{
        'key|+1': 1,
        'mockTitle|1': ['肆无忌惮'],
        'mockContent|1': ['角色精湛主题略荒诞', '理由太短 是让人不安', '疑信参半 却无比期盼', '你的惯犯 圆满', '别让纠缠 显得 孤单'],
        'mockAction|1': ['下载', '试听', '喜欢']
    }]
});
const data1 = Mock.mock({
    'status': 200,
    'dataSource|1-9': [{
        'key|+1': 1,
        'mockTitle|1': ['肆无忌惮'],
        'mockContent|1': ['角色精湛主题略荒诞', '理由太短 是让人不安', '疑信参半 却无比期盼', '你的惯犯 圆满', '别让纠缠 显得 孤单'],
        'mockAction|1': ['下载', '试听', '喜欢']
    }]
});
export default { data, data1 }