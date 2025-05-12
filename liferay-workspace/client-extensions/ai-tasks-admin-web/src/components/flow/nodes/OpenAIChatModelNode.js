/**
 * @author Petteri Karttunen
 */
import React, { memo } from 'react';

import BaseNode from './BaseNode';

const OpenAIChatModelNode = memo(({ data, id, type }) => {
  const { label, parameters } = data;
  const details = [
    ['modelName', parameters.modelName],
    ['temperature', parameters.temperature],
  ];

  return <BaseNode className="llm-node" details={details} id={id} label={label} type={type} />;
});

export default OpenAIChatModelNode;
