import { shallowMount } from '@vue/test-utils';
import axios from 'axios';
import LoginPage from '@/pages/LoginPage.vue';
import LoadingPage from "@/components/LoadingPage.vue";

jest.mock('axios');

describe('LoginPage.vue', () => {
  let wrapper;
  const pushMock = jest.fn();

  beforeEach(() => {
    wrapper = shallowMount(LoginPage, {
      global: {
        components: {
          LoadingPage
        },
        mocks: {
          $router: {
            push: pushMock
          }
        }
      }
    });

    // sessionStorage 모킹
    Object.defineProperty(window, 'sessionStorage', {
      value: {
        setItem: jest.fn()
      },
      writable: true
    });
  });

  it('네트워크 오류로 로그인 실패시 메시지 테스트', async () => {
    axios.post.mockRejectedValueOnce({
      response: { data: { code: "ERR_NETWORK", message: "Network Error" } }
    });

    await wrapper.vm.login();
    await wrapper.vm.$nextTick();

    expect(wrapper.vm.popUpStatus).toBeTruthy();
    expect(wrapper.vm.popText).toContain("서버 관리자에게 연락하세요.");
  });

  it('인증안된 사용자 로그인 실패 메시지 테스트', async () => {
    axios.post.mockRejectedValueOnce({
      response: { data: { code: "USER-004", message: "Invalid credentials" } }
    });

    await wrapper.vm.login();
    await wrapper.vm.$nextTick();

    expect(wrapper.vm.popUpStatus).toBeTruthy();
    expect(wrapper.vm.popText).toContain("다시 시도해주세요.");
  });

  it('성공적인 로그인 처리', async () => {
    axios.post.mockResolvedValueOnce({
      data: {
        code: 200,
        result: {
          token: 'testToken',
          refreshToken: 'testRefreshToken',
          name: 'testName'
        }
      }
    });

    await wrapper.vm.login();
    await wrapper.vm.$nextTick();

    expect(window.sessionStorage.setItem).toHaveBeenCalledWith('token', 'testToken');
    expect(window.sessionStorage.setItem).toHaveBeenCalledWith('refreshToken', 'testRefreshToken');
    expect(window.sessionStorage.setItem).toHaveBeenCalledWith('name', 'testName');
    expect(pushMock).toHaveBeenCalledWith('/main');
  });
});
