import { shallowMount } from '@vue/test-utils';
import LoginPage from '@/pages/LoginPage.vue';

describe('LoginPage.vue', () => {
  it('handles login error', async () => {
    const wrapper = shallowMount(LoginPage);
    await wrapper.vm.login(); // Simulate login action
    

    // You can assert how your component reacts to the login error here
    // For example:
    // expect(wrapper.vm.popTitle).toBe("회원가입에 실패하였습니다.");
    // expect(wrapper.vm.popText).toBe("다시 시도해주세요.");
  });
});