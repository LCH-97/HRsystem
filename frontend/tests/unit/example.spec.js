import { shallowMount } from '@vue/test-utils'
import SignUpPage from "@/pages/SignUpPage.vue";
// import axios from 'axios';
jest.mock(SignUpPage);
beforeEach(() => {
  SignUpPage.mockClear();
  
});
describe('SignUpPage', () => {
  it('renders props.msg when passed', () => {
    const msg = '회원가입'
    const wrapper = shallowMount(SignUpPage, {
      props: { msg }
    })
    expect(wrapper.text()).toMatch(msg)
  })
})
