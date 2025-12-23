const Card = ({ children, className = '', hover = false, ...props }) => {
  return (
    <div
      className={`card ${hover ? 'hover:shadow-lg transition-shadow cursor-pointer' : ''} ${className}`}
      {...props}
    >
      {children}
    </div>
  );
};

export default Card;
