export class FF {
    value?: any;
    flag?: string;

    public constructor(flag?: string, value?: boolean) {
        if (flag !== undefined) {
            this.flag = flag;
        }
        if (value !== undefined) {
            this.value = value;
        }
    }

    

    

}
