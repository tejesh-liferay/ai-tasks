/**
 * @author Petteri Karttunen
 */
import React from 'react';
import { useTranslation } from 'react-i18next';

const BooleanField = ({ className, helpText, label, onChange, parameterName, parameterValue }) => {
  const { t } = useTranslation();

  return (
    <div className={'form-group ' + (className ? className : '')}>
      <div className="custom-control custom-checkbox custom-control-outside">
        <label>
          <input
            className="custom-control-input"
            defaultChecked={parameterValue}
            id={parameterName}
            name={parameterName}
            onChange={(e) => {
              onChange(parameterName, e.currentTarget.value);
            }}
            type="checkbox"
            value={parameterValue}
          />
          <span className="custom-control-label">
            {helpText ? helpText : t(parameterName + '-help')}
          </span>
        </label>
      </div>
    </div>
  );
};

export default BooleanField;
