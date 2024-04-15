import { shallowMount } from '@vue/test-utils';
import axios from 'axios';
import DepartmentListModal from '@/components/DepartmentListModal.vue';

jest.mock('axios', () => ({
  get: jest.fn(() => Promise.resolve({ data: [{ id: '1', name: 'John Doe', username: 'johndoe' }] }))
}));

describe('DepartmentListModal.vue', () => {
  let wrapper;
  const departments = [
    { id: '1', departmentName: 'Development' },
    { id: '2', departmentName: 'Marketing' }
  ];

  beforeEach(() => {
    wrapper = shallowMount(DepartmentListModal, {
      props: {
        departments,
        isVisible: true,
        backend: 'http://test.backend'
      }
    });
  });

  it('부서명테스트', () => {
    expect(wrapper.text()).toContain('Development');
    expect(wrapper.text()).toContain('Marketing');
  });

  it('선택한 부서의 직원목록 불러오기 테스트', async () => {
    await wrapper.vm.selectDepartment('1');
    expect(axios.get).toHaveBeenCalledWith('http://test.backend/employee/department/1', expect.any(Object));
    expect(wrapper.vm.selectedEmployees).toEqual([{ id: '1', name: 'John Doe', username: 'johndoe' }]);
  });

  it('직원을 선택해서 결재자로 지정 테스트', async () => {
    // 직원 목록을 수동으로 설정합니다.
    wrapper.setData({ selectedEmployees: [{ id: '1', name: 'John Doe', username: 'johndoe' }] });
    await wrapper.vm.confirmSelection('1', 'John Doe');
    expect(wrapper.emitted().confirm).toBeTruthy();
    expect(wrapper.emitted().confirm[0]).toEqual([{ id: '1', name: 'John Doe' }]);
  });

  it('닫기버튼으로 모달창 닫는 테스트', async () => {
    await wrapper.find('button').trigger('click'); // 닫기 버튼을 클릭합니다.
    expect(wrapper.emitted().close).toBeTruthy(); // 'close' 이벤트가 방출되었는지 확인합니다.
  });
  
});
