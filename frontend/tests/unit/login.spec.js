import { shallowMount } from '@vue/test-utils';
import axios from 'axios';
import LoginPage from '@/pages/LoginPage.vue';
import LoadingPage from "@/components/LoadingPage.vue";

jest.mock('axios');

describe('LoginPage.vue', () => {

  it('handles login error', async () => {
    const wrapper = shallowMount(LoginPage);
    await wrapper.vm.login(); // Simulate login action
   

    expect(window.sessionStorage.setItem).toHaveBeenCalledWith('token', 'testToken');
    expect(window.sessionStorage.setItem).toHaveBeenCalledWith('refreshToken', 'testRefreshToken');
    expect(window.sessionStorage.setItem).toHaveBeenCalledWith('name', 'testName');
    expect(pushMock).toHaveBeenCalledWith('/main');
  });
});
