import { shallowMount } from '@vue/test-utils';
import axios from 'axios';
import GooutCreate from '@/components/goout/GooutCreate.vue';
import DepartmentListModal from '@/components/DepartmentListModal.vue';

jest.mock('axios');

describe('GooutCreate.vue', () => {
  let wrapper;

  beforeEach(() => {
    axios.get.mockResolvedValueOnce({
      data: { result: [{ id: '1', name: '근태유형1' }] } // 근태 유형 목록
    }).mockResolvedValueOnce({
      data: [{ id: '1', name: '직원1' }] // 직원 목록
    }).mockResolvedValueOnce({
      data: { result: [{ id: '1', name: '부서1' }] } // 부서 목록
    });

    wrapper = shallowMount(GooutCreate, {
      global: {
        stubs: { DepartmentListModal }
      }
    });
  });

  it('초기데이터(휴가타입) 가져오는 테스트', async () => {
    await wrapper.vm.$nextTick();
    expect(axios.get).toHaveBeenCalledTimes(3);
    expect(wrapper.vm.gooutTypes.length).toBeGreaterThan(0);
    expect(wrapper.vm.employees.length).toBeGreaterThan(0);
    expect(wrapper.vm.departments.length).toBeGreaterThan(0);
  });

  it('폼 제출 핸들러 테스트', async () => {
    const spy = jest.spyOn(wrapper.vm, 'createGooutRequest');
    await wrapper.vm.handleFormSubmission();
    expect(spy).toHaveBeenCalled();
  });

  it('파일 업로드 테스트', async () => {
    const files = [new File([''], 'testfile.txt')];
    await wrapper.vm.handleFilesUpload({ target: { files } });
    expect(wrapper.vm.files).toEqual(files);
  });

  it('결재자 선택 모달창 테스트', async () => {
    await wrapper.vm.showModalForConfirmer1();
    expect(wrapper.vm.isModalVisibleForConfirmer1).toBe(true);
  });

  // 추가적인 테스트 케이스는 컴포넌트의 기능과 요구사항에 따라 확장 가능합니다.
});
