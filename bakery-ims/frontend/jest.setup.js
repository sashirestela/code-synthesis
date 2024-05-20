global.bootstrap = {
    Modal: jest.fn(() => ({
        show: jest.fn(),
        hide: jest.fn(),
    })),
};