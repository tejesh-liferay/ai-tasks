/**
 * @author Louis-Guillaume Durand
 * @author Petteri Karttunen
 */
import React, { Children, cloneElement, useState } from 'react';

const TabContent = ({ activeTab, children }) => {
  return (
    <div className="tab-content">
      {Children.map(children, (child, index) => {
        if (!child.props.children) {
          return null;
        }

        return cloneElement(child.props.children, {
          className: `${
            child.props.className === undefined ? '' : child.props.className + ' '
          } fade tab-pane ${activeTab === index ? 'active show' : ''}`,
          'aria-labelledby': `${child.props.id}-tab`,
          id: child.props.id,
          role: 'tabpanel',
        });
      })}
    </div>
  );
};

export { TabContent };
