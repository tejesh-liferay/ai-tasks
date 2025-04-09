/**
 * @author Petteri Karttunen
 */
import React from 'react';
import { useTranslation } from 'react-i18next';

const BooleanField = ({ className, helpText, label, onChange, parameterName, parameterValue }) => {
  const { t } = useTranslation();

  return (
    <div className={'form-group ' + (className ? className : '')}>
      <label className="toggle-switch" htmlFor={parameterName}>
        <span className="toggle-switch-label">{label ? label : t(parameterName + '-label')}</span>
        <span className="toggle-switch-check-bar">
          <input
            id={parameterName}
            className="toggle-switch-check"
            role="switch"
            type="checkbox"
            value={parameterValue}
            onChange={(e) => {
              onChange(parameterName, e.currentTarget.checked);
            }}
            defaultChecked={parameterValue}
          />
          <span aria-hidden="true" className="toggle-switch-bar">
            <span className="toggle-switch-handle"></span>
          </span>
        </span>
        <span className="toggle-switch-text">
          <small className="form-text text-muted">
            {helpText ? helpText : t(parameterName + '-help')}
          </small>
        </span>
      </label>
    </div>
  );
};

export default BooleanField;
