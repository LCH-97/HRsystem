import { shallowMount } from '@vue/test-utils';
import axios from 'axios';
import GooutList from '@/components/goout/GooutList.vue';

jest.mock('axios');

describe('GooutList.vue', () => {
  let wrapper;

  beforeEach(() => {
    axios.get.mockClear();
    axios.get.mockResolvedValue({
      data: {
        code: 1200,
        isSuccess: true,
        result: {
          goouts: [{ id: 1, status: 0, name: 'Test Name', writerName: 'Writer', gooutTypeName: 'Annual', first: '2023-01-01', last: '2023-01-02' }],
          totalElements: 1,
        },
      },
    });

    wrapper = shallowMount(GooutList, {
      global: {
        mocks: {
          $router: {
            push: jest.fn(),
          },
        },
      },
    });
  });

  it('휴가가져오기', async () => {
    await wrapper.vm.$nextTick();
    expect(axios.get).toHaveBeenCalled();
  });

  it('필터기능', async () => {
    await wrapper.vm.$nextTick();
    wrapper.vm.filterGoouts(0); // 기안중
    expect(wrapper.vm.filteredGoouts.length).toBe(1);
    wrapper.vm.filterGoouts(null); // 전체
    expect(wrapper.vm.filteredGoouts.length).toBe(1);
  });

  it('페이지변경', async () => {
    wrapper.vm.changePage(2);
    expect(wrapper.vm.currentPage).toBe(2);
    // Ensure fetchGoouts is called when the page changes
    expect(axios.get).toHaveBeenCalledTimes(2);
  });

  it('휴가작성페이지 이동', async () => {
    await wrapper.vm.goToGooutCreate();
    expect(wrapper.vm.$router.push).toHaveBeenCalledWith('/goout/create');
  });
});
