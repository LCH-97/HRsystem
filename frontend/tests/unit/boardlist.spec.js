import { shallowMount } from '@vue/test-utils';
import BoardListPage from '@/pages/BoardListPage.vue';
import axios from 'axios';

jest.mock('axios');

describe('BoardListPage.vue', () => {
  it('fetches board data on mount', async () => {
    const mockData = {
      result: [
        { id: 1, filename: 'file1.pdf' },
        { id: 2, filename: 'file2.pdf' },
        // Add more mock data as needed
      ],
    };
    
    axios.get.mockResolvedValueOnce({ data: mockData });
    
    const wrapper = shallowMount(BoardListPage);
    
    await wrapper.vm.$nextTick();
    
    expect(wrapper.vm.boards).toEqual(mockData.result);
  });
  
  // Add more test cases as needed
});