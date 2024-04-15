import { shallowMount } from '@vue/test-utils';
import axios from 'axios';
import ApproveCreatePage from '@/pages/Approve/ApproveCreatePage.vue';
import DepartmentListModal from '@/components/DepartmentListModal.vue';

jest.mock('axios');

describe('ApproveCreatePage.vue', () => {
  let wrapper;

  // window.alert 모의 처리
  window.alert = jest.fn();

  beforeEach(() => {
    // axios.get 요청 모의 처리
    axios.get
      .mockResolvedValueOnce({ // fetchDepartments 호출에 대한 모의 구현
        data: { result: [{ id: '1', name: '부서1' }] }
      })
      .mockResolvedValueOnce({ // fetchEmployees 호출에 대한 모의 구현
        data: { result: [{ id: '1', name: '직원1' }] }
      });
  
    wrapper = shallowMount(ApproveCreatePage, {
      global: {
        stubs: { DepartmentListModal }
      },
      mocks: {
        $router: {
          push: jest.fn()
        }
      }
    });
  });


  it('결재 생성 요청 테스트', async () => {
    const spy = jest.spyOn(wrapper.vm, 'ApproveCreate');
    await wrapper.vm.handleFormSubmission();
    expect(spy).toHaveBeenCalled();
  });

  it('파일 업로드 테스트', async () => {
    const files = [new File([''], 'testfile.txt')];
    // 파일 업로드 처리 메서드를 직접 호출하고, 파일 배열을 인자로 전달합니다.
    wrapper.vm.handleFilesUpload({ target: { files } });
  
    await wrapper.vm.$nextTick(); // 컴포넌트 상태 업데이트를 기다립니다.
    expect(wrapper.vm.files.length).toBe(1);
    expect(wrapper.vm.files[0]).toEqual(files[0]);
  });
  

  it('결재자1 선택 모달창 테스트', async () => {
    await wrapper.vm.showModalForConfirmer1();
    expect(wrapper.vm.isModalVisibleForConfirmer1).toBe(true);
  });


  // 추가적인 테스트 케이스는 컴포넌트의 기능과 요구사항에 따라 확장 가능합니다.
});
