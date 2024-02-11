
export class LogMessage {
  dateTime?:string;
  message?:string;
  throwable?:any;
  image?:string;
  subSessionMessages?:LogMessage[]
  success?:boolean = true;
}
