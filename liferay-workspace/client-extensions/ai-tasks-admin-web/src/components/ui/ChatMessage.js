const ChatMessage = ({ role, metadata, children }) => {
  return (
    <div
      className={`chat-preview-message  d-flex mb-2 ${
        role === 'USER' ? 'justify-content-end' : 'justify-content-start'
      }`}
    >
      <div className={`message ${role === 'USER' ? 'user-message' : 'system-message'}`}>
        {children}
      </div>
    </div>
  );
};

export default ChatMessage;
