import { shallowMount } from '@vue/test-utils';
import axios from 'axios';
import ApproveListPage from '@/pages/Approve/ApproveListPage.vue';

jest.mock('axios');

describe('ApproveListPage.vue', () => {
  let wrapper;

  beforeEach(() => {
    axios.get.mockClear();
    axios.get.mockResolvedValue({
      data: {
        result: {
          content: [
            { id: 1, createAt: '2023-01-01', title: 'Test Title', employeeName: 'Employee 1', confirmer1Name: 'Confirmer 1', confirmer2Name: 'Confirmer 2', status: 0 },
          ],
        },
      },
    });

    wrapper = shallowMount(ApproveListPage, {
      global: {
        mocks: {
          $router: {
            push: jest.fn(),
          },
        },
      },
    });
  });

  it('결재 가져오기', async () => {
    await wrapper.vm.$nextTick();
    expect(axios.get).toHaveBeenCalled();
  });

  it('필터기능', async () => {
    await wrapper.vm.$nextTick();
    wrapper.vm.filterApprovalsByStatus(0); // 기안중
    expect(wrapper.vm.filteredApprovals.length).toBe(1);
    wrapper.vm.filterApprovalsByStatus(null); // 전체
    expect(wrapper.vm.filteredApprovals.length).toBe(1);
  });

  it('페이지 이동', async () => {
    wrapper.vm.changePage(2);
    expect(wrapper.vm.currentPage).toBe(2);
    expect(axios.get).toHaveBeenCalledTimes(2);
  });

  it('상세페이지 이동', async () => {
    const approveId = wrapper.vm.filteredApprovals[0].id;
    wrapper.vm.goToApproveReadPage(approveId);
    expect(wrapper.vm.$router.push).toHaveBeenCalledWith(`/approve/read/${approveId}`);
  });
});
