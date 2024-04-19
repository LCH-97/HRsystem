import { shallowMount } from '@vue/test-utils';
import axios from 'axios';
import SignUpPage from '@/pages/SignUpPage.vue';

jest.mock('axios');

describe('SignUpPage.vue', () => {
  let wrapper;

  beforeEach(() => {
    wrapper = shallowMount(SignUpPage, {
      data() {
        return {
          name: '',
          phoneNum: '',
          birth: '',
          address: '',
          positionId: '',
          departmentId: '',
          username: '',
          password: ''
        };
      }
    });
  });
  
  it('데이터 속성 입력 테스트', async () => {
    const testData = {
      name: 'test',
      phoneNum: '01012345678',
      birth: '1990-01-01',
      address: 'Some address',
      positionId: '1',
      departmentId: '1',
      username: 'johndoe',
      password: 'password123'
    };
    await wrapper.setData(testData);
    await wrapper.vm.$nextTick();

    for (const key in testData) {
      expect(wrapper.vm[key]).toBe(testData[key]);
    }
  });

  it('성공 팝업창 테스트', async () => {
    const response = { data: { code: 200 } };

    axios.post.mockResolvedValue(response);

    await wrapper.vm.register();
    await wrapper.vm.$nextTick();

    expect(wrapper.vm.popUpStatus).toBe(true);
    expect(wrapper.vm.responseData).toEqual(response.data);
  });

  // it('실패 메시지 테스트', async () => {
  //   const errorResponse = {
  //     response: {
  //       data: { code: 400, message: '다시 시도해주세요.' }
  //     }
  //   };
  
  //   axios.post.mockRejectedValue(errorResponse);
  
  //   await wrapper.vm.register();
  //   await wrapper.vm.$nextTick();
  
  //   expect(wrapper.vm.popTitle).toBe("회원가입에 실패하였습니다.");
  //   expect(wrapper.vm.popText).toBe("다시 시도해주세요.");
  // });
  
});