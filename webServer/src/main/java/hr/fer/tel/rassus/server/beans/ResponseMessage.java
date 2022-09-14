package hr.fer.tel.rassus.server.beans;

public class ResponseMessage {
		String message;
		String status;
		
		
		public ResponseMessage(String message, String status) {
			super();
			this.message = message;
			this.status = status;
		}
	
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		
}
