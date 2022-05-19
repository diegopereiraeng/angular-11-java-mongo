import { User } from './user.model';

describe('Repository', () => {
  it('should create an instance', () => {
    expect(new User()).toBeTruthy();
  });
});
