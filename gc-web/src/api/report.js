import request from '@/utils/request'

export function getPanelGroupData() {
  return request({
    url: 'api/report/total',
    method: 'get'
  })
}

export function getTotalLineChart(data) {
  return request({
    url: 'api/report/totalLineChart?type=' + data,
    method: 'get'
  })
}
