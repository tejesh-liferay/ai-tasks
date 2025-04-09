/**
 * @author Louis-Guillaume Durand
 * @author Petteri Karttunen
 */
import React, { Children, cloneElement, useState } from 'react';

const Tab = ({ id, label, className, children }) => {
  return { id, label, className, children };
};

const Tabs = ({ children, className, onChange }) => {
  const [activeTab, setActiveTab] = useState(0);

  const handleClick = (index) => {
    setActiveTab(index);
    if (onChange) {
      onChange(index); // Call the parent's onChange function
    }
  };

  return (
    <>
      <nav className="navbar navbar-collapse-relative navbar-expand-md navbar-underline navigation-bar navigation-bar-light">
        <div className="container-fluid container-fluid-max-xl">
          <ul className="navbar-nav">
            {Children.map(children, (child, index) => (
              <li key={index} className="nav-item">
                <a
                  aria-controls={child.props.id}
                  aria-selected={activeTab === index ? 'true' : 'false'}
                  className={`nav-link ${activeTab === index ? 'active' : ''}`}
                  data-toggle="tab"
                  href={`#${child.props.id}`}
                  id={`${child.props.id}-tab`}
                  role="tab"
                  onClick={(e) => {
                    e.preventDefault();
                    handleClick(index);
                  }}
                >
                  {child.props.label}
                </a>
              </li>
            ))}
          </ul>
        </div>
      </nav>
    </>
  );
};

export { Tabs, Tab };
