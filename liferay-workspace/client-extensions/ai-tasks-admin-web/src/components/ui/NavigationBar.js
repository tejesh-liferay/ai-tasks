/**
 * @author Louis-Guillaume Durand
 * @author Petteri Karttunen
 */
import React from 'react';

const NavigationBar = ({ children, ulClassName }) => {
  return (
    <nav className="tbar component-tbar tbar-light">
      <div className={'container-fluid container-fluid-max-xl'}>
        <ul className={'tbar-nav ' + (ulClassName ? ulClassName : '')}>{children}</ul>
      </div>
    </nav>
  );
};

export default NavigationBar;
