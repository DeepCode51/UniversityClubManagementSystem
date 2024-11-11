import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/users/loginIn',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/users/queryuserInfo',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/users/logout',
    method: 'post'
  })
}
